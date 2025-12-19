package com.mystika.tarot.cards;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.springframework.util.Assert;

public record TarotDeck(String name, String slug, List<TarotCard> cards) {

    public TarotDeck shuffle() {
        return cards.stream()
            .sorted(this::randomly)
            .collect(collectingAndThen(
                toList(),
                newCards -> new TarotDeck(name, slug, newCards)
            ));
    }

    public TarotCard drawOne() {
        return draw(1).findFirst()
            .orElseThrow();
    }

    public Stream<TarotCard> drawThree() {
        return draw(3);
    }

    public Stream<TarotCard> draw(int nrOfCards) {
        Assert.state(nrOfCards >= 1, "Must draw at least one card");
        Assert.state(nrOfCards <= cards.size(), "Cannot draw more cards than are in the deck");
        return ThreadLocalRandom.current()
            .ints(0, cards.size() - 1)
            .distinct()
            .limit(nrOfCards)
            .mapToObj(cards::get);
    }

    private int randomly(TarotCard c1, TarotCard c2) {
        return ThreadLocalRandom.current()
            .nextBoolean() ? -1 : 1;
    }
}
