package dev.jlipka.gamesstore.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class GameAddRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be lower than 255 characters")
    private final String name;

    @NotBlank(message = "Developer is required")
    @Size(max = 255, message = "Developer must be lower than 255 characters")
    private final String developer;

    @NotBlank(message = "Description is required")
    @Size(max = 8000, message = "Description must be lower than 8000 characters")
    private final String description;

    private final Long baseGameId;

    @NotBlank(message = "Publisher is required")
    @Size(max = 255, message = "Publisher must be lower than 255 characters")
    private final String publisher;


    private final LocalDate releaseDate;

    @NotNull
    @PositiveOrZero(message = "Base price must be greater or equal to zero")
    private final BigDecimal basePrice;

    @PositiveOrZero(message = "Discount price must be greater or equal to zero")
    private final BigDecimal discountedPrice;

    @Future(message = "Discount end date must be in future")
    private final LocalDateTime discountedUntil;

    @NotNull
    @JsonProperty("active")
    private final boolean isActive;

    @Valid
    private Set<@NotBlank(message = "Genre cannot be blank") String> genres;

    @Valid
    private Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 255) String> platforms;

    @Valid
    private Set<@NotBlank(message = "Language cannot be blank") @Size(max = 255) String> languages;

    @Valid
    private Set<@NotBlank(message = "Feature cannot be blank") @Size(max = 255) String> features;

    @Valid
    private Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 255) String> tags;


    public GameAddRequestDto(String name,
                             String developer,
                             String description,
                             Long baseGameId,
                             String publisher,
                             LocalDate releaseDate,
                             BigDecimal basePrice,
                             BigDecimal discountedPrice,
                             LocalDateTime discountedUntil,
                             boolean isActive,
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
        this.discountedPrice = discountedPrice;
        this.discountedUntil = discountedUntil;
        this.isActive = isActive;
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

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public LocalDateTime getDiscountedUntil() {
        return discountedUntil;
    }

    public boolean isActive() {
        return isActive;
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