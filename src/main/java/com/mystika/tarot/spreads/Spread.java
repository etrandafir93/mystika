package com.mystika.tarot.spreads;

public sealed interface Spread permits ThreeCardSpread {

    String id();

    String deckSlug();
}
