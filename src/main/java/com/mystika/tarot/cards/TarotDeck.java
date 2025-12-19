package com.mystika.tarot.cards;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.springframework.util.Assert;

public record TarotDeck(String name, String slug, List<TarotCard> cards) {

    public TarotDeck shuffle() {
        var newCards = new ArrayList<>(cards);
        Collections.shuffle(newCards);
        return new TarotDeck(name, slug, unmodifiableList(newCards));
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

}
