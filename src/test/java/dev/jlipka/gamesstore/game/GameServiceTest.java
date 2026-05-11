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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private FeatureService featureService;
    @Mock
    private GenreService genreService;
    @Mock
    private LanguageService languageService;
    @Mock
    private PlatformService platformService;
    @Mock
    private PublisherService publisherService;
    @Mock
    private TagService tagService;

    @InjectMocks
    private GameService gameService;

    private Publisher publisher;
    private Genre genre;
    private Platform platform;
    private Language language;
    private Feature feature;
    private Tag tag;

    @BeforeEach
    void setUp() {
        publisher = new Publisher();
        publisher.setName("Publisher Name");

        genre = new Genre();
        genre.setName("Genre 1");

        platform = new Platform();
        platform.setName("Platform 1");

        language = new Language();
        language.setName("Language 1");

        feature = new Feature();
        feature.setName("Feature 1");

        tag = new Tag();
        tag.setName("Tag 1");
    }

    private Game buildGame() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Game Name");
        game.setDeveloper("Developer Name");
        game.setDescription("Description");
        game.setBasePrice(new BigDecimal("60.00"));
        game.setReleaseDate(LocalDate.of(2015, 5, 19));
        game.setReleaseStatus(ReleaseStatusType.RELEASED);
        game.setActive(true);
        game.setPublisher(publisher);
        game.getGenres().add(genre);
        game.getPlatforms().add(platform);
        game.getLanguages().add(language);
        game.getFeatures().add(feature);
        game.getTags().add(tag);
        return game;
    }

    private GameAddRequestDto buildAddRequest() {
        return new GameAddRequestDto(
                "Game Name",
                "Developer Name",
                "Description",
                null,
                "Publisher Name",
                LocalDate.of(2015, 5, 19),
                new BigDecimal("60.00"),
                null,
                null,
                false,
                Set.of("Genre 1"),
                Set.of("Platform 1"),
                Set.of("Language 1"),
                Set.of("Tag 1"),
                Set.of("Feature 1")
        );
    }

    @Test
    void add_savesGame_whenRequestIsValid() {

        //given
        GameAddRequestDto request = buildAddRequest();
        Game savedGame = buildGame();

        when(gameRepository.existsByNameAndDeveloperAndReleaseDate(
                request.getName(), request.getDeveloper(), request.getReleaseDate())).thenReturn(false);
        when(publisherService.findOrCreate(request.getPublisher())).thenReturn(publisher);
        when(genreService.findOrCreate(anyString())).thenReturn(genre);
        when(platformService.findOrCreate(anyString())).thenReturn(platform);
        when(languageService.findOrCreate(anyString())).thenReturn(language);
        when(featureService.findOrCreate(anyString())).thenReturn(feature);
        when(tagService.findOrCreate(anyString())).thenReturn(tag);
        when(gameRepository.save(any())).thenReturn(savedGame);

        //when
        GameAdminFullDetailsDto result = gameService.add(request);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Game Name");
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void add_throwsException_whenGameAlreadyExists() {

        //given
        GameAddRequestDto request = buildAddRequest();

        when(gameRepository.existsByNameAndDeveloperAndReleaseDate(
                request.getName(), request.getDeveloper(), request.getReleaseDate())).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> {
            gameService.add(request);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_ALREADY_EXISTS);

        verify(gameRepository, never()).save(any());
    }

    @Test
    void add_throwsException_whenDiscountedPriceIsNotLowerThanBasePrice() {
        //given
        GameAddRequestDto request = new GameAddRequestDto(
                "Game Name",
                "Developer Name",
                "Description",
                null,
                "Publisher Name",
                LocalDate.of(2015, 5, 19),
                new BigDecimal("60.00"),
                new BigDecimal("60.00"),
                null,
                false,
                Set.of("Genre 1"),
                Set.of("Platform 1"),
                Set.of("Language 1"),
                Set.of("Tag 1"),
                Set.of("Feature 1")
        );

        when(gameRepository.existsByNameAndDeveloperAndReleaseDate(any(), any(), any())).thenReturn(false);
        when(publisherService.findOrCreate(anyString())).thenReturn(publisher);
        when(genreService.findOrCreate(anyString())).thenReturn(genre);
        when(platformService.findOrCreate(anyString())).thenReturn(platform);
        when(languageService.findOrCreate(anyString())).thenReturn(language);
        when(featureService.findOrCreate(anyString())).thenReturn(feature);
        when(tagService.findOrCreate(anyString())).thenReturn(tag);

        //when & then
        assertThatThrownBy(() -> {
            gameService.add(request);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_DISCOUNTED_PRICE);

        verify(gameRepository, never()).save(any());
    }

    @Test
    void add_throwsException_whenBaseGameIdNotFound() {

        //given
        GameAddRequestDto request = new GameAddRequestDto(
                "Game Name DLC",
                "Developer Name",
                "Description",
                99L,
                "Publisher Name",
                LocalDate.of(2015, 5, 19),
                new BigDecimal("20.00"),
                null,
                null,
                false,
                Set.of("Genre 1"),
                Set.of("Platform 1"),
                Set.of("Language 1"),
                Set.of("Tag 1"),
                Set.of("Feature 1")
        );

        when(gameRepository.existsByNameAndDeveloperAndReleaseDate(any(), any(), any())).thenReturn(false);
        when(publisherService.findOrCreate(anyString())).thenReturn(publisher);
        when(genreService.findOrCreate(anyString())).thenReturn(genre);
        when(platformService.findOrCreate(anyString())).thenReturn(platform);
        when(languageService.findOrCreate(anyString())).thenReturn(language);
        when(featureService.findOrCreate(anyString())).thenReturn(feature);
        when(tagService.findOrCreate(anyString())).thenReturn(tag);
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> {
            gameService.add(request);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_NOT_FOUND);

        verify(gameRepository, never()).save(any());
    }

    @Test
    void update_updatesGame_whenGameExists() {

        //given
        Game existingGame = buildGame();

        GameUpdateRequestDto request = new GameUpdateRequestDto();
        request.setName("Updated Game Name");
        request.setBasePrice(new BigDecimal("50.00"));

        when(gameRepository.findById(1L)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any())).thenReturn(existingGame);

        //when
        GameAdminFullDetailsDto result = gameService.update(request, 1L);

        //then
        assertThat(result).isNotNull();
        verify(gameRepository).save(existingGame);
        assertThat(existingGame.getName()).isEqualTo("Updated Game Name");
        assertThat(existingGame.getBasePrice()).isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    void update_throwsException_whenGameNotFound() {

        //given
        GameUpdateRequestDto request = new GameUpdateRequestDto();

        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> {
            gameService.update(request, 99L);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_NOT_FOUND);

        verify(gameRepository, never()).save(any());
    }

    @Test
    void update_throwsException_whenDiscountedPriceIsNotLowerThanBasePrice() {

        //given
        Game existingGame = buildGame();
        existingGame.setBasePrice(new BigDecimal("60.00"));

        GameUpdateRequestDto request = new GameUpdateRequestDto();
        request.setDiscountedPrice(new BigDecimal("70.00"));

        when(gameRepository.findById(1L)).thenReturn(Optional.of(existingGame));

        //when & then
        assertThatThrownBy(() -> {
            gameService.update(request, 1L);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_DISCOUNTED_PRICE);

        verify(gameRepository, never()).save(any());
    }

    @Test
    void update_throwsException_whenBaseGameIdNotFound() {

        //given
        Game existingGame = buildGame();

        GameUpdateRequestDto request = new GameUpdateRequestDto();
        request.setBaseGameId(99L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(existingGame));
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> {
            gameService.update(request, 1L);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_NOT_FOUND);

        verify(gameRepository, never()).save(any());
    }

    @Test
    void delete_deletesGame_whenGameExists() {

        //given
        Game existingGame = buildGame();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(existingGame));

        //when
        gameService.delete(1L);

        //then
        verify(gameRepository).delete(existingGame);
    }

    @Test
    void delete_throwsException_whenGameNotFound() {

        //given
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> {
            gameService.delete(99L);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_NOT_FOUND);

        verify(gameRepository, never()).delete(any(Game.class));
    }

    @Test
    void getGameFullDetails_returnsDto_whenGameExists() {

        //given
        Game existingGame = buildGame();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(existingGame));

        //when
        GameFullDetailsDto result = gameService.getGameFullDetails(1L);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Game Name");
        assertThat(result.getDeveloper()).isEqualTo("Developer Name");
    }

    @Test
    void getGameFullDetails_throwsException_whenGameNotFound() {

        //given
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> {
            gameService.getGameFullDetails(99L);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_NOT_FOUND);
    }

    @Test
    void getGameFullDetails_throwsException_whenGameIsNotActive() {

        //given
        Game existingGame = buildGame();
        existingGame.setActive(false);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(existingGame));

        //when & then
        assertThatThrownBy(() -> {
            gameService.getGameFullDetails(1L);
        })
                .isInstanceOf(GamesStoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_NOT_ACTIVE);
    }

    @Test
    void filter_returnsEmptyList_whenNoGamesMatch() {

        //given
        when(gameRepository.findAll(any(Specification.class))).thenReturn(List.of());

        GameFilterDto filterDto = new GameFilterDto();
        filterDto.setName("Nonexistent Game");

        //when
        List<GameListingDto> result = gameService.filter(filterDto);

        //then
        assertThat(result).isEmpty();
    }
}