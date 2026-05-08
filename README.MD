
# Local Development

1. Create db container using this command:

```bash
  docker pull postgres:latest
  docker run --name games_store_db -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres
```

2. 