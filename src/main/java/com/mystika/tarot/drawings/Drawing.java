package com.mystika.tarot.drawings;

public sealed interface Drawing permits ThreeCardSpread {

    String id();
}
