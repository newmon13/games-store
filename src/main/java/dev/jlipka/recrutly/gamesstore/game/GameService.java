package dev.jlipka.recrutly.gamesstore.game;

import dev.jlipka.recrutly.gamesstore.game.feature.Feature;
import dev.jlipka.recrutly.gamesstore.game.feature.FeatureService;
import dev.jlipka.recrutly.gamesstore.game.genre.Genre;
import dev.jlipka.recrutly.gamesstore.game.genre.GenreService;
import dev.jlipka.recrutly.gamesstore.game.language.Language;
import dev.jlipka.recrutly.gamesstore.game.language.LanguageService;
import dev.jlipka.recrutly.gamesstore.game.platform.Platform;
import dev.jlipka.recrutly.gamesstore.game.platform.PlatformService;
import dev.jlipka.recrutly.gamesstore.game.publisher.Publisher;
import dev.jlipka.recrutly.gamesstore.game.publisher.PublisherService;
import dev.jlipka.recrutly.gamesstore.game.tag.Tag;
import dev.jlipka.recrutly.gamesstore.game.tag.TagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class GameService {

    private static final int DAYS_GAME_REMAIN_NEW = 30;

    private final GameRepository gameRepository;
    private final FeatureService featureService;
    private final GenreService genreService;
    private final LanguageService languageService;
    private final PlatformService platformService;
    private final PublisherService publisherService;
    private final TagService tagService;

    public GameService(GameRepository gameRepository, FeatureService featureService, GenreService genreService, LanguageService languageService, PlatformService platformService, PublisherService publisherService, TagService tagService) {
        this.gameRepository = gameRepository;
        this.featureService = featureService;
        this.genreService = genreService;
        this.languageService = languageService;
        this.platformService = platformService;
        this.publisherService = publisherService;
        this.tagService = tagService;
    }


    public Game add(AddGameRequest request) {

        Game game = new Game();

        request.getFeatures().forEach(feature -> {
            String normalized = normalizeName(feature);

            Feature orCreate = featureService.findOrCreate(normalized);
            game.getFeatures().add(orCreate);
        });

        request.getGenres().forEach(genre   -> {
            String normalized = normalizeName(genre);

            Genre orCreate = genreService.findOrCreate(normalized);
            game.getGenres().add(orCreate);
        });

        request.getLanguages().forEach(language   -> {
            String normalized = normalizeName(language);

            Language orCreate = languageService.findOrCreate(normalized);
            game.getLanguages().add(orCreate);
        });

        request.getPlatforms().forEach(platform   -> {
            String normalized = normalizeName(platform);

            Platform orCreate = platformService.findOrCreate(normalized);
            game.getPlatforms().add(orCreate);
        });

        request.getTags().forEach(tag   -> {
            String normalized = normalizeName(tag);
            Tag orCreate = tagService.findOrCreate(normalized);
            game.getTags().add(orCreate);
        });


        String publisher = request.getPublisher();
        String normalized = normalizeName(publisher);
        Publisher orCreate = publisherService.findOrCreate(normalized);
        game.setPublisher(orCreate);


        game.setDescription(request.getDescription());
        game.setName(request.getName());
        game.setDeveloper(request.getDeveloper());

        game.setReleaseDate(request.getReleaseDate());

        if (request.getBaseGameId() != null) {
            gameRepository.findById(request.getBaseGameId())
                    .ifPresent(game::setBaseGame);
        }

        ReleaseStatusType releaseStatusType = calculateReleaseStatusType(request.getReleaseDate());
        game.setReleaseStatus(releaseStatusType);


        return gameRepository.save(game);
    }


    private ReleaseStatusType calculateReleaseStatusType(LocalDate releaseDate) {

        LocalDate today = LocalDate.now();

        if (releaseDate.isAfter(today)) {
            return ReleaseStatusType.UPCOMING;
        }

        long days = daysBetweenInclusive(releaseDate, today);

        if (days < DAYS_GAME_REMAIN_NEW) {
            return ReleaseStatusType.NEW;
        }

        if (releaseDate.isBefore(today)) {
            return ReleaseStatusType.RELEASED;
        }

        return ReleaseStatusType.UNKNOWN;
    }

    private long daysBetweenInclusive(LocalDate ld1, LocalDate ld2) {
        return Math.abs(ChronoUnit.DAYS.between(ld1, ld2)) + 1;
    }

    private String normalizeName(String value) {
        return StringUtils.capitalize(value.trim().toLowerCase());
    }
}
