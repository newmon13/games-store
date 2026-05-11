package dev.jlipka.gamesstore.game;

import dev.jlipka.gamesstore.game.genre.Genre;
import dev.jlipka.gamesstore.game.platform.Platform;
import dev.jlipka.gamesstore.game.language.Language;
import dev.jlipka.gamesstore.game.feature.Feature;
import dev.jlipka.gamesstore.game.tag.Tag;
import dev.jlipka.gamesstore.game.publisher.Publisher;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Set;

public class GameSpecification {

    public static Specification<Game> hasName(String name) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        });
    }

    public static Specification<Game> hasDeveloper(String developer) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("developer")), "%" + developer.toLowerCase() + "%");
    }

    public static Specification<Game> hasPublisher(String publisher) {
        return (root, query, cb) -> {
            Join<Game, Publisher> publisherJoin = root.join("publisher");
            return cb.like(cb.lower(publisherJoin.get("name")), "%" + publisher.toLowerCase() + "%");
        };
    }

    public static Specification<Game> hasPriceFrom(BigDecimal priceFrom) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("basePrice"), priceFrom);
    }

    public static Specification<Game> hasPriceTo(BigDecimal priceTo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("basePrice"), priceTo);
    }

    public static Specification<Game> hasGenres(Set<String> genres) {
        return (root, query, cb) -> {
            Join<Game, Genre> genreJoin = root.join("genres");
            query.distinct(true);
            return genreJoin.get("name").in(genres);
        };
    }

    public static Specification<Game> hasPlatforms(Set<String> platforms) {
        return (root, query, cb) -> {
            Join<Game, Platform> platformJoin = root.join("platforms");
            query.distinct(true);
            return platformJoin.get("name").in(platforms);
        };
    }

    public static Specification<Game> hasLanguages(Set<String> languages) {
        return (root, query, cb) -> {
            Join<Game, Language> languageJoin = root.join("languages");
            query.distinct(true);
            return languageJoin.get("name").in(languages);
        };
    }

    public static Specification<Game> hasFeatures(Set<String> features) {
        return (root, query, cb) -> {
            Join<Game, Feature> featureJoin = root.join("features");
            query.distinct(true);
            return featureJoin.get("name").in(features);
        };
    }

    public static Specification<Game> hasTags(Set<String> tags) {
        return (root, query, cb) -> {
            Join<Game, Tag> tagJoin = root.join("tags");
            query.distinct(true);
            return tagJoin.get("name").in(tags);
        };
    }

    public static Specification<Game> fromFilter(GameFilterDto filter) {
        Specification<Game> spec = Specification.where(null);

        if (filter.getName() != null) spec = spec.and(hasName(filter.getName()));
        if (filter.getDeveloper() != null) spec = spec.and(hasDeveloper(filter.getDeveloper()));
        if (filter.getPublisher() != null) spec = spec.and(hasPublisher(filter.getPublisher()));
        if (filter.getPriceFrom() != null) spec = spec.and(hasPriceFrom(filter.getPriceFrom()));
        if (filter.getPriceTo() != null) spec = spec.and(hasPriceTo(filter.getPriceTo()));
        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) spec = spec.and(hasGenres(filter.getGenres()));
        if (filter.getPlatforms() != null && !filter.getPlatforms().isEmpty()) spec = spec.and(hasPlatforms(filter.getPlatforms()));
        if (filter.getLanguages() != null && !filter.getLanguages().isEmpty()) spec = spec.and(hasLanguages(filter.getLanguages()));
        if (filter.getFeatures() != null && !filter.getFeatures().isEmpty()) spec = spec.and(hasFeatures(filter.getFeatures()));
        if (filter.getTags() != null && !filter.getTags().isEmpty()) spec = spec.and(hasTags(filter.getTags()));

        return spec;
    }
}