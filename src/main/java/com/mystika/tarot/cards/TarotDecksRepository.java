package com.mystika.tarot.cards;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tools.jackson.databind.json.JsonMapper;

@Repository
@RequiredArgsConstructor
public class TarotDecksRepository {

    private static final String DEFAULT_DECK = "rider-waite";

    private final JsonMapper json;

    @Getter(lazy = true)
    private final TarotDeck raiderWaite = loadDeckFromJson(DEFAULT_DECK, "Rider-Waite", Path.of("db/decks/rider-waite"));

    public Optional<TarotDeck> bySlug(String slug) {
        return switch (slug) {
            case DEFAULT_DECK -> Optional.of(raiderWaite());
            default -> Optional.empty();
        };
    }

    @SneakyThrows
    private TarotDeck loadDeckFromJson(String slug, String name, Path deckFolder) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:" + deckFolder + "/*.json");

        List<TarotCard> allCards = Arrays.stream(resources)
            .flatMap(resource -> loadCardsFromJson(resource).stream())
            .toList();
        return new TarotDeck(name, slug, allCards);
    }

    @SneakyThrows
    private List<TarotCard> loadCardsFromJson(Resource resource) {
        var listOfCards = json.getTypeFactory()
            .constructCollectionType(List.class, TarotCard.class);
        return json.readValue(resource.getInputStream(), listOfCards);
    }

}
