package com.mystika.tarot.cards;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@Repository
@RequiredArgsConstructor
public class TarotDecksRepository {

    private final JsonMapper json;

    @Getter(lazy = true)
    private final TarotDeck majorArcana = loadDeckFromJson("db/decks/major-arcana.json");

    public Optional<TarotDeck> bySlug(String slug) {
        if ("major-arcana".equalsIgnoreCase(slug)) {
            return Optional.of(majorArcana());
        }
        return Optional.empty();
    }

    private TarotDeck loadDeckFromJson(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            try (InputStream inputStream = resource.getInputStream()) {
                return json.readValue(inputStream, TarotDeck.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tarot cards from JSON", e);
        }
    }
}
