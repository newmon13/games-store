package dev.jlipka.gamesstore.game.feature;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeatureService {

    private final FeatureRepository featureRepository;

    public FeatureService(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }
    
    public Feature findOrCreate(String name) {
        Optional<Feature> byName = featureRepository.findByName(name);

        if (byName.isEmpty()) {
            Feature feature = new Feature();
            feature.setName(name);

            return featureRepository.save(feature);
        } else {
            return byName.get();
        }
    }

}
