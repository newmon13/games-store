package dev.jlipka.recrutly.gamesstore.game;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class AddGameRequest {

    private final String name;
    private final String developer;
    private final String description;

    private final Long baseGameId;

    private final String publisher;

    private final LocalDate releaseDate;

    private final BigDecimal basePrice;

    private final Set<String> genres;
    private final Set<String> platforms;
    private final Set<String> languages;
    private final Set<String> tags;
    private final Set<String> features;


    public AddGameRequest(String name,
                          String developer,
                          String description,
                          Long baseGameId,
                          String publisher,
                          LocalDate releaseDate,
                          BigDecimal basePrice,
                          Set<String> genres,
                          Set<String> platforms,
                          Set<String> languages,
                          Set<String> tags,
                          Set<String> features) {
        this.name = name;
        this.developer = developer;
        this.description = description;
        this.baseGameId = baseGameId;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.basePrice = basePrice;
        this.genres = genres;
        this.platforms = platforms;
        this.languages = languages;
        this.tags = tags;
        this.features = features;
    }

    public String getName() {
        return name;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getDescription() {
        return description;
    }

    public Long getBaseGameId() {
        return baseGameId;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public Set<String> getPlatforms() {
        return platforms;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Set<String> getFeatures() {
        return features;
    }
}
