package dev.jlipka.recrutly.gamesstore.game;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @GetMapping
    public ResponseEntity<List<GameListingDto>> filterGames(@ModelAttribute @Valid GameFilterDto filterDto) {
        List<GameListingDto> allGameListings = gameService.filter(filterDto);
        return ResponseEntity.of(Optional.of(allGameListings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameFullDetailsDto> getGame(@PathVariable Long id) {
        GameFullDetailsDto gameFullDetails = gameService.getGameFullDetails(id);
        return ResponseEntity.of(Optional.of(gameFullDetails));
    }
}
