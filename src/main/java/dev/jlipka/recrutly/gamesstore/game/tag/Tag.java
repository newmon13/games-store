package dev.jlipka.recrutly.gamesstore.game.tag;

import dev.jlipka.recrutly.gamesstore.game.Game;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Game> games;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
