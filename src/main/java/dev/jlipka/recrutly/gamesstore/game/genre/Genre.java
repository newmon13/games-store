package dev.jlipka.recrutly.gamesstore.game.genre;

import dev.jlipka.recrutly.gamesstore.game.Game;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Game> games;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
