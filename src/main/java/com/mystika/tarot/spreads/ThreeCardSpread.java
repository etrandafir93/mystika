package com.mystika.tarot.spreads;

import static java.util.Collections.emptyList;

import java.util.List;

import com.mystika.tarot.reading.DrawnCard;

import lombok.With;

public record ThreeCardSpread(String id, String deckSlug, @With List<DrawnCard> cards) implements Spread {

    public ThreeCardSpread(String id, String deckSlug) {
        this(id, deckSlug, emptyList());
    }
}
