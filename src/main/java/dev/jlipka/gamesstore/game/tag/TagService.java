package dev.jlipka.gamesstore.game.tag;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findOrCreate(String name) {
        Optional<Tag> byName = tagRepository.findByName(name);

        if (byName.isEmpty()) {
            Tag tag = new Tag();
            tag.setName(name);

            return tagRepository.save(tag);
        } else {
            return byName.get();
        }
    }
}
