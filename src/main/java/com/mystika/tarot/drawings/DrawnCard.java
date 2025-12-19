package com.mystika.tarot.drawings;

import java.util.List;

public record DrawnCard(int position, String cardSlug, String name, Orientation orientation, String meaning, List<String> symbols, String imageUrl) {

    public enum Orientation {
        UPRIGHT,
        REVERSED;
    }
}
