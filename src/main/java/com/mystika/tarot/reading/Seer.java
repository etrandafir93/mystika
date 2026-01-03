package com.mystika.tarot.reading;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mystika.tarot.reading.Reading.Chapter;
import com.mystika.tarot.spreads.Spread;
import com.mystika.tarot.spreads.SpreadFocus;
import com.mystika.tarot.spreads.ThreeCardSpread;

@Component
class Seer {

    private static final List<String> chapters = List.of("Past", "Present", "Future");

    public Reading basicReading(Spread spread, SpreadFocus focus) {
        final Iterator<String> chaptersIt = chapters.iterator();
        return switch (spread) {
            case ThreeCardSpread(String _, String _, List<DrawnCard> cards) ->
                cards.stream()
                    .map(card -> card.detailedMeaning(focus))
                    .map(meaning -> new Chapter(chaptersIt.next(), meaning))
                    .collect(collectingAndThen(toList(), Reading::new));
        };
    }


}
