package dev.jlipka.gamesstore.game;

import dev.jlipka.gamesstore.game.feature.Feature;
import dev.jlipka.gamesstore.game.feature.FeatureRepository;
import dev.jlipka.gamesstore.game.genre.Genre;
import dev.jlipka.gamesstore.game.genre.GenreRepository;
import dev.jlipka.gamesstore.game.language.Language;
import dev.jlipka.gamesstore.game.language.LanguageRepository;
import dev.jlipka.gamesstore.game.platform.Platform;
import dev.jlipka.gamesstore.game.platform.PlatformRepository;
import dev.jlipka.gamesstore.game.publisher.Publisher;
import dev.jlipka.gamesstore.game.publisher.PublisherRepository;
import dev.jlipka.gamesstore.game.tag.Tag;
import dev.jlipka.gamesstore.game.tag.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GameRepositoryIntegrationTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher dummyPublisher;
    private Publisher otherPublisher;
    private Genre rpgGenre;
    private Genre actionGenre;
    private Platform pcPlatform;
    private Tag openWorldTag;
    private Language englishLanguage;
    private Feature achievementsFeature;

    @BeforeEach
    void setUp() {
        dummyPublisher = new Publisher();
        dummyPublisher.setName("Dummy Publisher");
        dummyPublisher = publisherRepository.save(dummyPublisher);

        otherPublisher = new Publisher();
        otherPublisher.setName("Other Publisher");
        otherPublisher = publisherRepository.save(otherPublisher);

        rpgGenre = new Genre();
        rpgGenre.setName("Rpg");
        rpgGenre = genreRepository.save(rpgGenre);

        actionGenre = new Genre();
        actionGenre.setName("Action");
        actionGenre = genreRepository.save(actionGenre);

        pcPlatform = new Platform();
        pcPlatform.setName("Pc");
        pcPlatform = platformRepository.save(pcPlatform);

        openWorldTag = new Tag();
        openWorldTag.setName("Open world");
        openWorldTag = tagRepository.save(openWorldTag);

        englishLanguage = new Language();
        englishLanguage.setName("English");
        englishLanguage = languageRepository.save(englishLanguage);

        achievementsFeature = new Feature();
        achievementsFeature.setName("Achievements");
        achievementsFeature = featureRepository.save(achievementsFeature);
    }

    private void buildAndSaveGame(String name, String developer, Publisher publisher, BigDecimal basePrice, Genre genre, Platform platform, Tag tag) {
        Game game = new Game();
        game.setName(name);
        game.setDeveloper(developer);
        game.setDescription("Dummy Description");
        game.setBasePrice(basePrice);
        game.setReleaseDate(LocalDate.of(2015, 5, 19));
        game.setReleaseStatus(ReleaseStatusType.RELEASED);
        game.setPublisher(publisher);
        game.getGenres().add(genre);
        game.getPlatforms().add(platform);
        game.getTags().add(tag);
        game.getLanguages().add(englishLanguage);
        game.getFeatures().add(achievementsFeature);
        gameRepository.save(game);
    }

    @Test
    void filter_returnsGames_whenNameMatches() {

        //given
        buildAndSaveGame("Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);
        buildAndSaveGame("Different Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);

        GameFilterDto filterDto = new GameFilterDto();
        filterDto.setName("Dummy Game");

        Specification<Game> spec = GameSpecification.fromFilter(filterDto);

        //when
        List<Game> result = gameRepository.findAll(spec);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("Dummy Game");
        assertThat(result.getLast().getName()).isEqualTo("Different Dummy Game");
    }

    @Test
    void filter_returnsGame_whenDeveloperMatches() {

        //given
        buildAndSaveGame("Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);
        buildAndSaveGame("Different Dummy Game", "Other Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);

        GameFilterDto filterDto = new GameFilterDto();
        filterDto.setDeveloper("Dummy Developer");

        Specification<Game> spec = GameSpecification.fromFilter(filterDto);

        //when
        List<Game> result = gameRepository.findAll(spec);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Dummy Game");
    }

    @Test
    void filter_returnsGame_whenPublisherMatches() {

        //given
        buildAndSaveGame("Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);
        buildAndSaveGame("Different Dummy Game", "Dummy Developer", otherPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);

        GameFilterDto filterDto = new GameFilterDto();
        filterDto.setPublisher("Dummy Publisher");

        Specification<Game> spec = GameSpecification.fromFilter(filterDto);

        //when
        List<Game> result = gameRepository.findAll(spec);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Dummy Game");
    }

    @Test
    void filter_returnsGame_whenGenreMatches() {

        //given
        buildAndSaveGame("Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);
        buildAndSaveGame("Different Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), actionGenre, pcPlatform, openWorldTag);

        GameFilterDto filterDto = new GameFilterDto();
        filterDto.setGenres(Set.of("Rpg"));

        Specification<Game> spec = GameSpecification.fromFilter(filterDto);

        //when
        List<Game> result = gameRepository.findAll(spec);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Dummy Game");
    }

    @Test
    void filter_returnsGame_whenPriceRangeMatches() {

        //given
        buildAndSaveGame("Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("40.00"), rpgGenre, pcPlatform, openWorldTag);
        buildAndSaveGame("Different Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("80.00"), rpgGenre, pcPlatform, openWorldTag);

        GameFilterDto filterDto = new GameFilterDto();
        filterDto.setPriceFrom(new BigDecimal("30.00"));
        filterDto.setPriceTo(new BigDecimal("50.00"));

        Specification<Game> spec = GameSpecification.fromFilter(filterDto);

        //when
        List<Game> result = gameRepository.findAll(spec);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Dummy Game");
    }

    @Test
    void filter_returnsAllGames_whenNoFiltersProvided() {

        //given
        buildAndSaveGame("Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);
        buildAndSaveGame("Different Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);

        Specification<Game> spec = GameSpecification.fromFilter(new GameFilterDto());

        //when
        List<Game> result = gameRepository.findAll(spec);

        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void filter_returnsEmpty_whenNoGamesMatch() {

        //given
        buildAndSaveGame("Dummy Game", "Dummy Developer", dummyPublisher, new BigDecimal("60.00"), rpgGenre, pcPlatform, openWorldTag);

        GameFilterDto filterDto = new GameFilterDto();
        filterDto.setName("Nonexistent Game");

        Specification<Game> spec = GameSpecification.fromFilter(filterDto);

        //when
        List<Game> result = gameRepository.findAll(spec);

        //then
        assertThat(result).isEmpty();
    }
}