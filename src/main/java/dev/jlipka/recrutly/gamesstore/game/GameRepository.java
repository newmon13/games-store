package dev.jlipka.recrutly.gamesstore.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{
    boolean existsByNameAndDeveloperAndReleaseDate(String name, String developer, LocalDate releaseDate);
}
