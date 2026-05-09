package dev.jlipka.recrutly.gamesstore.game;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/internal/games")
public class GameAdminController {

    private final GameService gameService;

    public GameAdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameAdminFullDetailsDto> add(@Validated @RequestBody GameAddRequestDto gameAddRequestDto) {
        return ResponseEntity.ok(gameService.add(gameAddRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GameAdminFullDetailsDto> update(@Validated @RequestBody GameUpdateRequestDto gameUpdateRequestDto, @PathVariable Long id) {
        return ResponseEntity.of(Optional.of(gameService.update(gameUpdateRequestDto, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
