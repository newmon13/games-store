# Games Store

A REST API for an online digital games store.
Users can browse and search games and admins can manage games.

Built with Java 21, Spring Boot 3.4, and H2 (in-memory) for zero-setup local development.

## How to run

```bash
./mvnw spring-boot:run
```

The application starts on http://localhost:8080.
The H2 console is available at http://localhost:8080/h2-console

url: jdbc:h2:mem:gamesstore

user: sa

password: password

## Quick start

File `src/main/resources/admin-story.http` contains ready to run HTTP requests covering the main flows.
Open it in IntelliJ IDEA to run individual requests directly after launching application.

Admin credentials: admin admin


## Architecture

### Package structure

The project is organized by feature, not by layer. Everything related to games lives under `game/`
Infra related things are located under `infra/` folder.
This makes the domain boundaries explicit and keeps related code close together.

### Separation of public and admin APIs

The client-facing API `/api/v1/games/**` and the admin API `/api/v1/internal/games/**` are implemented as two separate controllers. 

You can see http://localhost:8080/swagger-ui/index.html

### Domain

`Game` is the aggregator and parent entity. Rather than storing arrays of strings for platforms, tags, etc. - they are normalized into their own child entities This prevents data inconsistency and enables nice filtering options.

A game can be a DLC of another game via a self-reference.

The `isActive` flag represents that a game is published and visible to users. An admin can create a game in an unpublished state and activate it later.
Filtering by `isActive` is an internal concern — it is applied implicitly in the service layer and is not exposed as a filter parameter to the public API.

`ReleaseStatusType` is derived from the release date and recalculated on every write. For example a game is considered `NEW` for 30 days after release.

Prices use `BigDecimal` to avoid floating-point rounding issues.

### Filtering and pagination

The public listing endpoint supports filtering by: 
* name
* developer
* publisher
* price range
* genres
* platforms
* languages
* features
* tags

Filters are created dynamically using JPA `Specification`, so only the filter options that are actually provided get added to the query.

Results are paginated. The default page size is 20, sorted by name. Clients can control pagination and sorting.

### Error handling

All domain errors are represented as `GamesStoreException`. The `GlobalExceptionHandler` maps each code to the appropriate HTTP status and a consistent response body. This keeps error-handling logic out of the controllers.

```json
{
  "code": 1001,
  "message": "Game with given id was not found",
  "cause": "Resource does not exist",
  "action": "Check if the id is correct"
}
```

---


## Design decisions and trade-offs

1. H2 in-memory database

H2 was chosen to allow the application to start and be fully testable with no external dependencies like starting docker container with a db running.
The schema is created on startup and dropped on shutdown.

In production this would be replaced with any non-in-memory db.
A production setup would require also move credentials out of `application.properties` and into environment variables.

2.  In-memory authentication and basic auth

Admin authentication uses `InMemoryUserDetailsManager` with a hardcoded admin admin user.
This is ok for demonstrating the access-control behavior 
without the complexity of a user management system or jwt token generation or setup for jwt providers.
In production this would be replaced with JWT-based authentication and a persistent user store.

3.  Collection filter semantics

Filtering by collections (genres, tags...) uses an `IN` - this matches games that have any of the provided values.
The current behavior matches how most store catalogues work, at least I think they work like that.

### What I would add next

- Replacing in-memory db for a production-like setup
- JWT authentication replacing Basic Auth
- Caching on the listing endpoint for read heavy trafic


## Testing strategy

Tests are organized at three levels:

**Unit tests**
`GameServiceTest` test the service layer ins isolation using Mockito. Cover the main business rules: duplicate detection, discount price validation, DLC linking, `isActive` flag check.

**Integration tests** spins  up an in-memory database and verify that the dynamic filtering produces correct SQL and returns the right results.

**End-to-end tests**  tests the full request/response cycle including authentication, serialization, and HTTP status codes.
Two scenarios are covered: the full admin lifecycle (add -> search -> get -> update -> delete) and the anonymous user permissions.
