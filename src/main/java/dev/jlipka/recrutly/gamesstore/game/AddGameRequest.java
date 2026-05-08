package dev.jlipka.recrutly.gamesstore.game;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class AddGameRequest {

    @NotNull
    @Size(max = 255)
    private final String name;

    @NotNull
    @Size(max = 255)
    private final String developer;

    @NotNull
    @Size(max = 8000)
    private final String description;

    private final Long baseGameId;

    @NotNull
    @Size(max = 255)
    private final String publisher;

    private final LocalDate releaseDate;

    @Positive
    private final BigDecimal basePrice;

    @Valid
    Set<@NotBlank(message = "Genre cannot be blank") String> genres;

    @Valid
    Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 255) String> platforms;

    @Valid
    Set<@NotBlank(message = "Language cannot be blank") @Size(max = 255) String> languages;

    @Valid
    Set<@NotBlank(message = "Feature cannot be blank") @Size(max = 255) String> features;

    @Valid
    Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 255) String> tags;


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
