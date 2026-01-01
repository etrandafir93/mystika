package com.mystika.tarot.reading;

import java.util.List;
import java.util.Optional;

import com.mystika.tarot.cards.CardMeaning;
import com.mystika.tarot.spreads.SpreadFocus;

public record DrawnCard(int position, String cardSlug, String name, Orientation orientation, String meaning, List<String> symbols, String imageUrl,
                        CardMeaning cardMeaning) {

    public enum Orientation {
        UPRIGHT,
        REVERSED;
    }

    public String detailedMeaning(SpreadFocus focus) {
        if (cardMeaning == null) {
            return basicMeaning();
        }
        return switch (focus) {
            case LOVE -> cardMeaning.love().get(orientation);
            case CAREER -> cardMeaning.career().get(orientation);
            case SPIRITUALITY -> cardMeaning.spirituality().get(orientation);
        };
    }

    private String basicMeaning() {
        return "The %s card \"%s\" suggests that you have recently experienced significant events that have shaped your current situation: %s."
            .formatted(orientation == DrawnCard.Orientation.REVERSED ? "reversed " : "", name, meaning);
    }
}
