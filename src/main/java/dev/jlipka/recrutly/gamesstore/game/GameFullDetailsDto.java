package dev.jlipka.recrutly.gamesstore.game;

import dev.jlipka.recrutly.gamesstore.game.review.ReviewDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class GameFullDetailsDto {

    private String name;

    private String developer;

    private String description;

    private List<GameReferenceDto> dlcs;

    private GameReferenceDto baseGame;

    private String publisher;

    private LocalDate releaseDate;

    private BigDecimal avgRating;

    private BigDecimal basePrice;

    private BigDecimal discountedPrice;

    private LocalDateTime discountedUntil;

    private int reviewCount;

    private ReleaseStatusType releaseStatus;

    private Set<String> platforms;

    private Set<String> tags;

    private Set<String> languages;

    private Set<String> features;

    private Set<String> genres;

    private List<ReviewDto> reviews;


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

    public List<GameReferenceDto> getDlcs() {
        return dlcs;
    }

    public void setDlcs(List<GameReferenceDto> dlcs) {
        this.dlcs = dlcs;
    }

    public GameReferenceDto getBaseGame() {
        return baseGame;
    }

    public void setBaseGame(GameReferenceDto baseGame) {
        this.baseGame = baseGame;
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

    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
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

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public ReleaseStatusType getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(ReleaseStatusType releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public Set<String> getPlatforms() {
        return platforms;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setPlatforms(Set<String> platforms) {
        this.platforms = platforms;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}
