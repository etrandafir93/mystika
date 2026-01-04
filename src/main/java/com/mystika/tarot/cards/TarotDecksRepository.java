package com.mystika.tarot.cards;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import tools.jackson.databind.json.JsonMapper;

@Repository
public class TarotDecksRepository {

    public static final String RIDER_WAITE = "rider-waite";
    private final JsonMapper json;
    private TarotDeck raiderWaite;

    public TarotDecksRepository(JsonMapper json) {
        this.json = json;
    }

    public TarotDeck raiderWaite() {
        if (raiderWaite == null) {
            raiderWaite = loadDeckFromJson(RIDER_WAITE, "Rider-Waite", Path.of("db/decks/rider-waite"));
        }
        return raiderWaite;
    }

    public Optional<TarotDeck> bySlug(String slug) {
        return switch (slug) {
            case RIDER_WAITE -> Optional.of(raiderWaite());
            default -> Optional.empty();
        };
    }

    private TarotDeck loadDeckFromJson(String slug, String name, Path deckFolder) {
        try {
            return Arrays.stream(new PathMatchingResourcePatternResolver().getResources("classpath:%s/*.json".formatted(deckFolder)))
                .flatMap(resource -> loadCardsFromJson(resource).stream())
                .collect(collectingAndThen(toList(), allCards -> new TarotDeck(name, slug, allCards)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<TarotCard> loadCardsFromJson(Resource resource) {
        try {
            var listOfCards = json.getTypeFactory()
                .constructCollectionType(List.class, TarotCard.class);
            return json.readValue(resource.getInputStream(), listOfCards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
