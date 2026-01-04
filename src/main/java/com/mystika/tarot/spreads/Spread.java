package com.mystika.tarot.spreads;

import java.util.List;

import com.mystika.tarot.reading.DrawnCard;

public sealed interface Spread permits ThreeCardSpread {

    String id();

    String deckSlug();

    List<DrawnCard> cards();
}
