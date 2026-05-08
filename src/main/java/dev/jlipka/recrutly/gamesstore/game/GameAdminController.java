package dev.jlipka.recrutly.gamesstore.game;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/games")
public class GameAdminController {

    private final GameService gameService;

    public GameAdminController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody AddGameRequest addGameRequest) {
        return ResponseEntity.ok(gameService.add(addGameRequest));
    }
}
