package com.mystika.tarot.drawings;

import java.util.List;

record DrawnCard(int position, String cardSlug, String name, Orientation orientation, String meaning, List<String> symbols, String imageUrl) {

    enum Orientation {
        UPRIGHT,
        REVERSED;
    }
}
