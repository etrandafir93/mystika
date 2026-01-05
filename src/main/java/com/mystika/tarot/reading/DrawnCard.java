package com.mystika.tarot.reading;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mystika.tarot.cards.TarotCard;
import com.mystika.tarot.spreads.SpreadFocus;

public record DrawnCard(
    @JsonIgnore TarotCard card,
    int position,
    Orientation orientation
) {

    public enum Orientation {
        UPRIGHT,
        REVERSED;
    }

    @JsonProperty
    public String cardSlug() {
        return card.slug();
    }

    @JsonProperty
    public String name() {
        return card.name();
    }

    @JsonProperty
    public String suite() {
        return card.suite();
    }

    @JsonProperty
    public List<String> symbols() {
        return card.symbols();
    }

    @JsonProperty
    public String imageUrl() {
        return card.imageUrl();
    }

    @JsonProperty
    public String meaning() {
        return orientation == Orientation.REVERSED
            ? card.reversedMeaning()
            : card.meaning();
    }

    @JsonProperty
    public String detailedMeaning(SpreadFocus focus) {
        if (card.detailedMeaning() == null) {
            return basicMeaning();
        }
        return switch (focus) {
            case LOVE -> card.detailedMeaning().love().get(orientation);
            case CAREER -> card.detailedMeaning().career().get(orientation);
            case SPIRITUALITY -> card.detailedMeaning().spirituality().get(orientation);
        };
    }

    private String basicMeaning() {
        return "The %s card \"%s\" suggests that you have recently experienced significant events that have shaped your current situation: %s."
            .formatted(orientation == Orientation.REVERSED ? "reversed " : "", name(), meaning());
    }

    @Override
    public String toString() {
        return "DrawnCard{position=%d, card=%s, orientation=%s}"
            .formatted(position, card.name(), orientation);
    }
}
