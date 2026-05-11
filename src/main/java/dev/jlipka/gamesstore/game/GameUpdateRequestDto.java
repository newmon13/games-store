package dev.jlipka.gamesstore.game;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class GameUpdateRequestDto {

    @Size(max = 255, message = "Name must be lower than 255 characters")
    private String name;

    @Size(max = 255, message = "Developer must be lower than 255 characters")
    private String developer;

    @Size(max = 8000, message = "Description must be lower than 8000 characters")
    private String description;

    private Long baseGameId;

    @Size(max = 255, message = "Publisher must be lower than 255 characters")
    private String publisher;

    private LocalDate releaseDate;

    @PositiveOrZero(message = "Base price must be greater or equal to zero")
    private BigDecimal basePrice;

    @PositiveOrZero(message = "Discount price must be greater or equal to zero")
    private BigDecimal discountedPrice;

    @Future(message = "Discount end date must be in future")
    private LocalDateTime discountedUntil;

    private Boolean isActive;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBaseGameId() {
        return baseGameId;
    }

    public void setBaseGameId(Long baseGameId) {
        this.baseGameId = baseGameId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public LocalDateTime getDiscountedUntil() {
        return discountedUntil;
    }

    public void setDiscountedUntil(LocalDateTime discountedUntil) {
        this.discountedUntil = discountedUntil;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    public Set<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<String> platforms) {
        this.platforms = platforms;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}