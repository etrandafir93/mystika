package com.mystika.tarot.cards;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TarotDeck(String name, String slug, List<TarotCard> cards, @JsonIgnore RandomCardPicker cardPicker) {

    TarotDeck(String name, String slug, List<TarotCard> cards) {
        this(name, slug, unmodifiableList(cards), RandomCardPicker.fromThreadLocal());
    }

    public TarotDeck shuffle() {
        var shuffledCards = cardPicker.shuffle(cards);
        return new TarotDeck(name, slug, shuffledCards, cardPicker);
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
        return cardPicker.randomCardPositions(0, cards.size() - 1)
            .distinct()
            .limit(nrOfCards)
            .mapToObj(cards::get);
    }

    @FunctionalInterface
    interface RandomCardPicker {
        IntStream randomCardPositions(int origin, int bound);

        default List<TarotCard> shuffle(List<TarotCard> cards) {
            List<TarotCard> newCards = new ArrayList<>(cards);
            Collections.shuffle(newCards);
            return unmodifiableList(newCards);
        }

        static RandomCardPicker fromThreadLocal() {
            return ThreadLocalRandom.current()::ints;
        }
    }

}
