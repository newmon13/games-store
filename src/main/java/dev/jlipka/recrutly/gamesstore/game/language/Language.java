package dev.jlipka.recrutly.gamesstore.game.language;

import dev.jlipka.recrutly.gamesstore.game.Game;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "languages")
    private Set<Game> games;

    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
