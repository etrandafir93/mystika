package com.mystika.tarot.reading;

public sealed interface Drawing permits ThreeCardSpread {

    String id();
}
