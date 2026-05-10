package dev.jlipka.recrutly.gamesstore.game;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public class GameFilterDto {


    @Size(max = 255, message = "Name must be lower than 255 characters")
    private String name;

    @Size(max = 255, message = "Developer must be lower than 255 characters")
    private String developer;

    @Size(max = 255, message = "Publisher must be lower than 255 characters")
    private String publisher;

    @PositiveOrZero(message = "Price range lower bound must be greater or equal to zero")
    private BigDecimal priceFrom;

    @PositiveOrZero(message = "Price range upper bound must be greater or equal to zero")
    private BigDecimal priceTo;

    @Valid
    private Set<@NotBlank(message = "Genre cannot be blank") @Size(max = 255, message = "Genre must be lower than 255 characters") String> genres;

    @Valid
    private Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 255, message = "Platform must be lower than 255 characters") String> platforms;

    @Valid
    private Set<@NotBlank(message = "Language cannot be blank") @Size(max = 255, message = "Language must be lower than 255 characters") String> languages;

    @Valid
    private Set<@NotBlank(message = "Feature cannot be blank") @Size(max = 255, message = "Feature must be lower than 255 characters") String> features;

    @Valid
    private Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 255, message = "Tag must be lower than 255 characters") String> tags;

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public Set<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<String> platforms) {
        this.platforms = platforms;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }
}
