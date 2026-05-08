package dev.jlipka.recrutly.gamesstore.game.language;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }


    public Language findOrCreate(String name) {
        Optional<Language> byName = languageRepository.findByName(name);

        if (byName.isEmpty()) {
            Language language = new Language();
            language.setName(name);

            return languageRepository.save(language);
        } else {
            return byName.get();
        }
    }
}
