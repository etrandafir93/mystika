package com.mystika.tarot.cards;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
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

    private record Interpretation(String slug, String upright, String reversed) {}

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
            var resolver = new PathMatchingResourcePatternResolver();

            var baseCards = loadBaseCards(resolver.getResource("classpath:%s/cards.json".formatted(deckFolder)));

            var love          = loadInterpretations(resolver.getResource("classpath:%s/interpretations/love.json".formatted(deckFolder)));
            var career        = loadInterpretations(resolver.getResource("classpath:%s/interpretations/career.json".formatted(deckFolder)));
            var spirituality  = loadInterpretations(resolver.getResource("classpath:%s/interpretations/spirituality.json".formatted(deckFolder)));
            var dev           = loadInterpretations(resolver.getResource("classpath:%s/interpretations/dev.json".formatted(deckFolder)));
            var climbing      = loadInterpretations(resolver.getResource("classpath:%s/interpretations/climbing.json".formatted(deckFolder)));

            var cards = baseCards.stream()
                .map(card -> new TarotCard(
                    card.slug(), card.suite(), card.name(), card.meaning(), card.reversedMeaning(),
                    card.symbols(), card.imageUrl(),
                    new CardMeaning(love.get(card.slug()), career.get(card.slug()), spirituality.get(card.slug()), dev.get(card.slug()), climbing.get(card.slug()))
                ))
                .toList();

            return new TarotDeck(name, slug, cards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<TarotCard> loadBaseCards(Resource resource) throws IOException {
        var listType = json.getTypeFactory().constructCollectionType(List.class, TarotCard.class);
        return json.readValue(resource.getInputStream(), listType);
    }

    private Map<String, CardMeaning.Focus> loadInterpretations(Resource resource) throws IOException {
        var listType = json.getTypeFactory().constructCollectionType(List.class, Interpretation.class);
        List<Interpretation> list = json.readValue(resource.getInputStream(), listType);
        return list.stream().collect(toMap(Interpretation::slug, i -> new CardMeaning.Focus(i.upright(), i.reversed())));
    }

}
