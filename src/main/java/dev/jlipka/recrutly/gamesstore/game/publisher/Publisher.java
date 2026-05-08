package dev.jlipka.recrutly.gamesstore.game.publisher;

import dev.jlipka.recrutly.gamesstore.game.Game;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "publisher")
    private Set<Game> games;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
