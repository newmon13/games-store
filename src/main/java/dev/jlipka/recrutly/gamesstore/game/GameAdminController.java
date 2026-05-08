package dev.jlipka.recrutly.gamesstore.game;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internal/games")
public class GameAdminController {

    private final GameService gameService;

    public GameAdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGames() {
        List<Game> games = gameService.findAll();
        return ResponseEntity.of(Optional.of(games));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        Optional<Game> game = gameService.find(id);
        return ResponseEntity.of(game);
    }


    @PostMapping
    public ResponseEntity<Game> add(@Validated @RequestBody AddGameRequest addGameRequest) {
        return ResponseEntity.ok(gameService.add(addGameRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
