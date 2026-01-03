package com.mystika.tarot.cards;

import com.mystika.tarot.reading.DrawnCard;

public record CardMeaning(Focus love, Focus career, Focus spirituality) {

    public record Focus(String upright, String reversed) {

        public String get(DrawnCard.Orientation orientation) {
            return switch (orientation) {
                case UPRIGHT -> upright;
                case REVERSED -> reversed;
            };
        }

    }
}
