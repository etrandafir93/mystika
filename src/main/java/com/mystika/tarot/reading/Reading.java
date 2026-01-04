package com.mystika.tarot.reading;

import java.util.List;

public record Reading(List<Chapter> chapters) {

    public record Chapter(String title, String content) {

        @Override
        public String toString() {
            return "Chapter{ title='%s', content='%s' }"
                .formatted(title, content.substring(0, 20) + "...");
        }
    }

}
