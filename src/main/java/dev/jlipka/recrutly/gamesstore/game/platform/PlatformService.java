package dev.jlipka.recrutly.gamesstore.game.platform;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }


    public Platform findOrCreate(String name) {
        Optional<Platform> byName = platformRepository.findByName(name);

        if (byName.isEmpty()) {
            Platform platform = new Platform();
            platform.setName(name);

            return platformRepository.save(platform);
        } else {
            return byName.get();
        }
    }
}
