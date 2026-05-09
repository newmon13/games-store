package dev.jlipka.recrutly.gamesstore.game.publisher;

import dev.jlipka.recrutly.gamesstore.game.platform.Platform;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }


    public Publisher findOrCreate(String name) {
        Optional<Publisher> byName = publisherRepository.findByName(name);

        if (byName.isEmpty()) {
            Publisher publisher = new Publisher();
            publisher.setName(name);

            return publisherRepository.save(publisher);
        } else {
            return byName.get();
        }
    }
}
