package dev.jlipka.recrutly.gamesstore.game.feature;

import dev.jlipka.recrutly.gamesstore.game.Game;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "features")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "features")
    private Set<Game> games;


    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
