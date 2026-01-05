package com.mystika.tarot.reading;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mystika.tarot.cards.TarotCard;
import com.mystika.tarot.cards.TarotDeck;
import com.mystika.tarot.cards.TarotDecksRepository;
import com.mystika.tarot.spreads.Spread;
import com.mystika.tarot.spreads.ThreeCardSpread;

@Component
class Seeker {

    private final TarotDecksRepository decks;

    public ThreeCardSpread draw(Spread spread) {
        return switch (spread) {
            case ThreeCardSpread threeCardSpread -> draw(threeCardSpread);
        };
    }

    private ThreeCardSpread draw(ThreeCardSpread spread) {
        TarotDeck deck = decks.bySlug(spread.deckSlug())
            .orElseThrow();

        AtomicInteger position = new AtomicInteger(1);
        List<DrawnCard> drawnCards = deck
            .shuffle()
            .drawThree()
            .map(card -> drawnTarotCard(position.getAndIncrement(), card))
            .toList();

        return spread.withCards(drawnCards);
    }

    static DrawnCard drawnTarotCard(int position, TarotCard card) {
        var orientation = weightedRandom();
        return new DrawnCard(card, position, orientation);
    }

    static DrawnCard.Orientation weightedRandom() {
        return ThreadLocalRandom.current()
            .nextInt(100) <= 80 ? DrawnCard.Orientation.UPRIGHT : DrawnCard.Orientation.REVERSED;
    }

    Seeker(TarotDecksRepository decks) {
        this.decks = decks;
    }
}
