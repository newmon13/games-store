package dev.jlipka.gamesstore.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game>{
    boolean existsByNameAndDeveloperAndReleaseDate(String name, String developer, LocalDate releaseDate);
}
