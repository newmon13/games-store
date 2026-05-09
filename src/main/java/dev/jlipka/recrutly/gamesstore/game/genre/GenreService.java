package dev.jlipka.recrutly.gamesstore.game.genre;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    public Genre findOrCreate(String name) {
        Optional<Genre> byName = genreRepository.findByName(name);

        if (byName.isEmpty()) {
            Genre genre = new Genre();
            genre.setName(name);

            return genreRepository.save(genre);
        } else {
            return byName.get();
        }
    }
}
