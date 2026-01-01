package com.mystika.tarot.cards;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static final String RIDER_WAITE = "rider-waite";

    private final JsonMapper json;

    @Getter(lazy = true)
    private final TarotDeck raiderWaite = loadDeckFromJson(RIDER_WAITE, "Rider-Waite", Path.of("db/decks/rider-waite"));

    public Optional<TarotDeck> bySlug(String slug) {
        return switch (slug) {
            case RIDER_WAITE -> Optional.of(raiderWaite());
            default -> Optional.empty();
        };
    }

    @SneakyThrows
    private TarotDeck loadDeckFromJson(String slug, String name, Path deckFolder) {
        Resource[] resources = new PathMatchingResourcePatternResolver()
            .getResources("classpath:%s/*.json".formatted(deckFolder));

        return Arrays.stream(resources)
            .flatMap(resource -> loadCardsFromJson(resource).stream())
            .collect(collectingAndThen(
                toList(),
                allCards -> new TarotDeck(name, slug, allCards)
            ));
    }

    @SneakyThrows
    private List<TarotCard> loadCardsFromJson(Resource resource) {
        var listOfCards = json.getTypeFactory()
            .constructCollectionType(List.class, TarotCard.class);
        return json.readValue(resource.getInputStream(), listOfCards);
    }

}
