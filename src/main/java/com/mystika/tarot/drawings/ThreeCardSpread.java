package com.mystika.tarot.drawings;

import static java.util.Collections.emptyList;

import java.util.List;

import lombok.With;

public record ThreeCardSpread(String id, String deckSlug, @With List<DrawnCard> cards) implements Drawing {

    ThreeCardSpread(String id, String deckSlug) {
        this(id, deckSlug, emptyList());
    }
}
