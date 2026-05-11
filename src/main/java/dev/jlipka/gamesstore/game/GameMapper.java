package dev.jlipka.gamesstore.game;

import dev.jlipka.gamesstore.game.feature.Feature;
import dev.jlipka.gamesstore.game.genre.Genre;
import dev.jlipka.gamesstore.game.language.Language;
import dev.jlipka.gamesstore.game.platform.Platform;
import dev.jlipka.gamesstore.game.tag.Tag;

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

        if (game.getBaseGame() != null) {
            gameFullDetailsDto.setBaseGame(mapGameToGameReferenceDto(game.getBaseGame()));
        }
        gameFullDetailsDto.setPublisher(game.getPublisher().getName());
        gameFullDetailsDto.setReleaseDate(game.getReleaseDate());
        gameFullDetailsDto.setBasePrice(game.getBasePrice());
        gameFullDetailsDto.setDiscountedPrice(game.getDiscountedPrice());
        gameFullDetailsDto.setDiscountedUntil(game.getDiscountedUntil());
        gameFullDetailsDto.setReleaseStatus(game.getReleaseStatus());
        gameFullDetailsDto.setFeatures(game.getFeatures().stream().map(Feature::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setTags(game.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setPlatforms(game.getPlatforms().stream().map(Platform::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setGenres(game.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()));
        gameFullDetailsDto.setLanguages(game.getLanguages().stream().map(Language::getName).collect(Collectors.toSet()));

        return gameFullDetailsDto;
    }

    public static GameAdminFullDetailsDto mapGameToGameAdminFullDetailsDto(Game game) {

        GameAdminFullDetailsDto gameAdminFullDetailsDto = new GameAdminFullDetailsDto();
        gameAdminFullDetailsDto.setId(game.getId());
        gameAdminFullDetailsDto.setName(game.getName());
        gameAdminFullDetailsDto.setDeveloper(game.getDeveloper());
        gameAdminFullDetailsDto.setDescription(game.getDescription());


        List<GameReferenceDto> dlcs = game.getDlcs().stream().map(GameMapper::mapGameToGameReferenceDto).toList();

        gameAdminFullDetailsDto.setDlcs(dlcs);

        if (game.getBaseGame() != null) {
            gameAdminFullDetailsDto.setBaseGame(mapGameToGameReferenceDto(game.getBaseGame()));
        }
        gameAdminFullDetailsDto.setPublisher(game.getPublisher().getName());
        gameAdminFullDetailsDto.setReleaseDate(game.getReleaseDate());
        gameAdminFullDetailsDto.setBasePrice(game.getBasePrice());
        gameAdminFullDetailsDto.setDiscountedPrice(game.getDiscountedPrice());
        gameAdminFullDetailsDto.setDiscountedUntil(game.getDiscountedUntil());
        gameAdminFullDetailsDto.setReleaseStatus(game.getReleaseStatus());
        gameAdminFullDetailsDto.setActive(game.isActive());
        gameAdminFullDetailsDto.setFeatures(game.getFeatures().stream().map(Feature::getName).collect(Collectors.toSet()));
        gameAdminFullDetailsDto.setTags(game.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        gameAdminFullDetailsDto.setPlatforms(game.getPlatforms().stream().map(Platform::getName).collect(Collectors.toSet()));
        gameAdminFullDetailsDto.setGenres(game.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()));
        gameAdminFullDetailsDto.setLanguages(game.getLanguages().stream().map(Language::getName).collect(Collectors.toSet()));
        gameAdminFullDetailsDto.setActive(game.isActive());


        return gameAdminFullDetailsDto;
    }

    public static GameReferenceDto mapGameToGameReferenceDto(Game game) {

        GameReferenceDto gameReferenceDto = new GameReferenceDto();
        gameReferenceDto.setId(game.getId());
        gameReferenceDto.setName(game.getName());

        return gameReferenceDto;
    }
}
