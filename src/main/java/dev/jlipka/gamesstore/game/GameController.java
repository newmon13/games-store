package dev.jlipka.gamesstore.game;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @GetMapping
    public ResponseEntity<Page<GameListingDto>> filterGames(
            @ModelAttribute @Valid GameFilterDto filterDto,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(gameService.filter(filterDto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameFullDetailsDto> getGame(@PathVariable Long id) {
        GameFullDetailsDto gameFullDetails = gameService.getGameFullDetails(id);
        return ResponseEntity.ok(gameFullDetails);
    }
}
