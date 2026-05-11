package dev.jlipka.gamesstore.game;

import dev.jlipka.gamesstore.game.feature.Feature;
import dev.jlipka.gamesstore.game.feature.FeatureService;
import dev.jlipka.gamesstore.game.genre.Genre;
import dev.jlipka.gamesstore.game.genre.GenreService;
import dev.jlipka.gamesstore.game.language.Language;
import dev.jlipka.gamesstore.game.language.LanguageService;
import dev.jlipka.gamesstore.game.platform.Platform;
import dev.jlipka.gamesstore.game.platform.PlatformService;
import dev.jlipka.gamesstore.game.publisher.Publisher;
import dev.jlipka.gamesstore.game.publisher.PublisherService;
import dev.jlipka.gamesstore.game.tag.Tag;
import dev.jlipka.gamesstore.game.tag.TagService;
import dev.jlipka.gamesstore.infra.error.ErrorCode;
import dev.jlipka.gamesstore.infra.error.GamesStoreException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static dev.jlipka.gamesstore.game.GameMapper.*;

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

    public Page<GameListingDto> filter(GameFilterDto gameFilterDto, Pageable pageable) {
        Specification<Game> spec = GameSpecification.fromFilter(gameFilterDto)
                .and((root, query, cb) -> cb.isTrue(root.get("isActive")));
        return gameRepository.findAll(spec, pageable).map(GameMapper::mapGameToGameListingItemDto);
    }

    @Transactional
    public GameAdminFullDetailsDto add(GameAddRequestDto request) {

        if (gameRepository.existsByNameAndDeveloperAndReleaseDate(request.getName(),
                request.getDeveloper(),
                request.getReleaseDate())) {
            throw new GamesStoreException(ErrorCode.GAME_ALREADY_EXISTS);
        }

        Game game = new Game();

        request.getFeatures().forEach(feature -> {
            String normalized = normalizeName(feature);
            Feature orCreate = featureService.findOrCreate(normalized);
            game.getFeatures().add(orCreate);
        });

        request.getGenres().forEach(genre -> {
            String normalized = normalizeName(genre);
            Genre orCreate = genreService.findOrCreate(normalized);
            game.getGenres().add(orCreate);
        });

        request.getLanguages().forEach(language -> {
            String normalized = normalizeName(language);
            Language orCreate = languageService.findOrCreate(normalized);
            game.getLanguages().add(orCreate);
        });

        request.getPlatforms().forEach(platform -> {
            String normalized = normalizeName(platform);
            Platform orCreate = platformService.findOrCreate(normalized);
            game.getPlatforms().add(orCreate);
        });

        request.getTags().forEach(tag -> {
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
        game.setBasePrice(request.getBasePrice());
        game.setReleaseDate(request.getReleaseDate());
        game.setActive(request.isActive());

        if (request.getDiscountedPrice() != null) {
            if (request.getDiscountedPrice().compareTo(request.getBasePrice()) >= 0) {
                throw new GamesStoreException(ErrorCode.INVALID_DISCOUNTED_PRICE);
            }
            game.setDiscountedPrice(request.getDiscountedPrice());
        }

        if (request.getDiscountedUntil() != null) {
            game.setDiscountedUntil(request.getDiscountedUntil());
        }

        if (request.getBaseGameId() != null) {
            Game baseGame = gameRepository.findById(request.getBaseGameId())
                    .orElseThrow(() -> new GamesStoreException(ErrorCode.GAME_NOT_FOUND));
            game.setBaseGame(baseGame);
        }

        ReleaseStatusType releaseStatusType = calculateReleaseStatusType(request.getReleaseDate());
        game.setReleaseStatus(releaseStatusType);

        Game save = gameRepository.save(game);
        return mapGameToGameAdminFullDetailsDto(save);
    }

    @Transactional
    public GameAdminFullDetailsDto update(GameUpdateRequestDto request, Long id) {
        Optional<Game> byId = gameRepository.findById(id);

        if (byId.isEmpty()) {
            throw new GamesStoreException(ErrorCode.GAME_NOT_FOUND);
        }

        Game game = byId.get();

        if (request.getName() != null) game.setName(request.getName());
        if (request.getDeveloper() != null) game.setDeveloper(request.getDeveloper());
        if (request.getDescription() != null) game.setDescription(request.getDescription());
        if (request.getBasePrice() != null) game.setBasePrice(request.getBasePrice());
        if (request.getIsActive() != null) game.setActive(request.getIsActive());

        if (request.getDiscountedPrice() != null) {
            BigDecimal basePriceToCompare;

            if (request.getBasePrice() != null) {
                basePriceToCompare = request.getBasePrice();
            } else {
                basePriceToCompare = game.getBasePrice();
            }

            if (request.getDiscountedPrice().compareTo(basePriceToCompare) >= 0) {
                throw new GamesStoreException(ErrorCode.INVALID_DISCOUNTED_PRICE);
            }

            game.setDiscountedPrice(request.getDiscountedPrice());
        }

        if (request.getDiscountedUntil() != null) game.setDiscountedUntil(request.getDiscountedUntil());

        if (request.getReleaseDate() != null) {
            game.setReleaseDate(request.getReleaseDate());
            game.setReleaseStatus(calculateReleaseStatusType(request.getReleaseDate()));
        }

        if (request.getBaseGameId() != null) {
            Game baseGame = gameRepository.findById(request.getBaseGameId())
                    .orElseThrow(() -> new GamesStoreException(ErrorCode.GAME_NOT_FOUND));
            game.setBaseGame(baseGame);
        }

        if (request.getPublisher() != null) {
            Publisher publisher = publisherService.findOrCreate(normalizeName(request.getPublisher()));
            game.setPublisher(publisher);
        }

        if (request.getGenres() != null) {
            game.getGenres().clear();
            request.getGenres().forEach(genre ->
                    game.getGenres().add(genreService.findOrCreate(normalizeName(genre))));
        }

        if (request.getTags() != null) {
            game.getTags().clear();
            request.getTags().forEach(tag ->
                    game.getTags().add(tagService.findOrCreate(normalizeName(tag))));
        }

        if (request.getLanguages() != null) {
            game.getLanguages().clear();
            request.getLanguages().forEach(language ->
                    game.getLanguages().add(languageService.findOrCreate(normalizeName(language))));
        }

        if (request.getPlatforms() != null) {
            game.getPlatforms().clear();
            request.getPlatforms().forEach(platform ->
                    game.getPlatforms().add(platformService.findOrCreate(normalizeName(platform))));
        }

        if (request.getFeatures() != null) {
            game.getFeatures().clear();
            request.getFeatures().forEach(feature ->
                    game.getFeatures().add(featureService.findOrCreate(normalizeName(feature))));
        }

        Game save = gameRepository.save(game);
        return mapGameToGameAdminFullDetailsDto(save);
    }

    @Transactional
    public void delete(Long id) {

        Optional<Game> byId = gameRepository.findById(id);

        if (byId.isEmpty()) {
            throw new GamesStoreException(ErrorCode.GAME_NOT_FOUND);
        }

        Game game = byId.get();

        game.getFeatures().clear();
        game.getTags().clear();
        game.getGenres().clear();
        game.getPlatforms().clear();
        game.getLanguages().clear();

        gameRepository.delete(game);
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

    public GameFullDetailsDto getGameFullDetails(Long id) {
        Optional<Game> byId = gameRepository.findById(id);

        if (byId.isEmpty()) {
            throw new GamesStoreException(ErrorCode.GAME_NOT_FOUND);
        }

        Game game = byId.get();

        if (!game.isActive()) {
            throw new GamesStoreException(ErrorCode.GAME_NOT_ACTIVE);
        }

        return mapGameToFullDetailsDto(byId.get());
    }
}