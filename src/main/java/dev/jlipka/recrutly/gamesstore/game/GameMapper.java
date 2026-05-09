package dev.jlipka.recrutly.gamesstore.game;

import dev.jlipka.recrutly.gamesstore.game.feature.Feature;
import dev.jlipka.recrutly.gamesstore.game.genre.Genre;
import dev.jlipka.recrutly.gamesstore.game.language.Language;
import dev.jlipka.recrutly.gamesstore.game.platform.Platform;
import dev.jlipka.recrutly.gamesstore.game.review.Review;
import dev.jlipka.recrutly.gamesstore.game.review.ReviewDto;
import dev.jlipka.recrutly.gamesstore.game.tag.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class GameMapper {

    public static GameListingDto mapGameToGameListingItemDto(Game game) {

        GameListingDto gameListingDto = new GameListingDto();
        gameListingDto.setId(game.getId());
        gameListingDto.setName(game.getName());
        gameListingDto.setBasePrice(game.getBasePrice());
        gameListingDto.setDiscountedPrice(game.getDiscountedPrice());

        return gameListingDto;
    }

    public static GameFullDetailsDto mapGameToFullDetailsDto(Game game) {

        GameFullDetailsDto gameFullDetailsDto = new GameFullDetailsDto();
        gameFullDetailsDto.setName(game.getName());
        gameFullDetailsDto.setDeveloper(game.getDeveloper());
        gameFullDetailsDto.setDescription(game.getDescription());


        List<GameReferenceDto> dlcs = game.getDlcs().stream().map(GameMapper::mapGameToGameReferenceDto).toList();

        gameFullDetailsDto.setDlcs(dlcs);

        gameFullDetailsDto.setBaseGame(mapGameToGameReferenceDto(game.getBaseGame()));
        gameFullDetailsDto.setPublisher(game.getPublisher().getName());
        gameFullDetailsDto.setReleaseDate(game.getReleaseDate());
        gameFullDetailsDto.setAvgRating(game.getAvgRating());
        gameFullDetailsDto.setReviews(game.getReviews().stream().map(GameMapper::mapReviewToReviewDto).toList());
        gameFullDetailsDto.setBasePrice(game.getBasePrice());
        gameFullDetailsDto.setDiscountedPrice(game.getDiscountedPrice());
        gameFullDetailsDto.setDiscountedUntil(game.getDiscountedUntil());
        gameFullDetailsDto.setReviewCount(game.getReviewCount());
        gameFullDetailsDto.setReleaseStatus(game.getReleaseStatus());
        gameFullDetailsDto.setFeatures(game.getFeatures().stream().map(Feature::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setTags(game.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setPlatforms(game.getPlatforms().stream().map(Platform::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setGenres(game.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setLanguages(game.getLanguages().stream().map(Language::getName).collect(Collectors.toSet()));

        return gameFullDetailsDto;
    }

    public static GameReferenceDto mapGameToGameReferenceDto(Game game) {

        GameReferenceDto gameReferenceDto = new GameReferenceDto();
        gameReferenceDto.setId(game.getId());
        gameReferenceDto.setName(game.getName());

        return gameReferenceDto;
    }

    public static ReviewDto mapReviewToReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setTitle(review.getTitle());
        reviewDto.setDescription(review.getDescription());
        reviewDto.setRating(review.getRating());
        return reviewDto;
    }
}
