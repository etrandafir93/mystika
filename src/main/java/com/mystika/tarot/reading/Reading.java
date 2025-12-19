package com.mystika.tarot.reading;

import java.util.List;

public record Reading (List<Chapter> chapters) {

    public record Chapter (String title, String content) {}

}
