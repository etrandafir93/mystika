package com.mystika.tarot.cards;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cards")
class CardsController {

    @GetMapping
    TarotDeck cards() {
        return new TarotDeck(List.of(
            new TarotCard("The Fool", "New beginnings, optimism, trust in life"),
            new TarotCard("The Magician", "Action, the power to manifest"),
            new TarotCard("The High Priestess", "Inaction, going within, the subconscious")
        ));
    }

    record TarotDeck(List<TarotCard> deck) { }

    record TarotCard(String name, String meaning) { }
}
