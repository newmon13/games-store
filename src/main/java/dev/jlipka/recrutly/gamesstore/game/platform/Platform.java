package dev.jlipka.recrutly.gamesstore.game.platform;

import dev.jlipka.recrutly.gamesstore.game.Game;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "platforms")
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "platforms")
    private Set<Game> games;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
