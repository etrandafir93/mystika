package com.mystika.tarot.cards;

import java.util.List;

public record TarotCard(
    String slug,
    String suite,
    String name,
    String meaning,
    String reversedMeaning,
    List<String> symbols,
    String imageUrl,
    CardMeaning detailedMeaning
) { }
