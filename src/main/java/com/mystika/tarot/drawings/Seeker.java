package com.mystika.tarot.drawings;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mystika.tarot.cards.TarotCard;
import com.mystika.tarot.cards.TarotDeck;
import com.mystika.tarot.cards.TarotDecksRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class Seeker {

    private final TarotDecksRepository decks;

    ThreeCardSpread draw(Drawing drawing) {
        return switch (drawing) {
            case ThreeCardSpread threeCardSpread -> draw(threeCardSpread);
        };
    }

    private ThreeCardSpread draw(ThreeCardSpread drawing) {
        TarotDeck deck = decks.bySlug(drawing.deckSlug())
            .orElseThrow();

        AtomicInteger position = new AtomicInteger(1);
        List<DrawnCard> drawnCards = deck
            .shuffle()
            .drawThree()
            .map(card -> drawnTarotCard(position.getAndIncrement(), card))
            .toList();

        return drawing.withCards(drawnCards);
    }

    static DrawnCard drawnTarotCard(int position, TarotCard card) {
        var orientation = weightedRandom();
        String meaning = orientation == DrawnCard.Orientation.UPRIGHT ? card.meaning() : card.reversedMeaning();
        return new DrawnCard(position, card.slug(), card.name(), orientation, meaning, card.symbols(), card.imageUrl());
    }

    static DrawnCard.Orientation weightedRandom() {
        return ThreadLocalRandom.current()
            .nextInt(100) <= 80 ? DrawnCard.Orientation.UPRIGHT : DrawnCard.Orientation.REVERSED;
    }
}
