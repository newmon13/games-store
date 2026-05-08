package dev.jlipka.recrutly.gamesstore.game.genre;

import dev.jlipka.recrutly.gamesstore.game.feature.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
