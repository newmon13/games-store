package dev.jlipka.recrutly.gamesstore.game.review;

import dev.jlipka.recrutly.gamesstore.game.Game;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;


//    @ManyToOne
//    private User author;

    private String title;

    private String description;

    private int rating;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
