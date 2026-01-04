package com.mystika.tarot.spreads;

import static java.util.Collections.emptyList;

import java.util.List;

import com.mystika.tarot.reading.DrawnCard;

public record ThreeCardSpread(String id, String deckSlug, List<DrawnCard> cards) implements Spread {

    public ThreeCardSpread(String id, String deckSlug) {
        this(id, deckSlug, emptyList());
    }

    public ThreeCardSpread withCards(List<DrawnCard> cards) {
        return new ThreeCardSpread(this.id, this.deckSlug, cards);
    }
}
