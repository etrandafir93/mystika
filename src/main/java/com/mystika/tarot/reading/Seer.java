package com.mystika.tarot.reading;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mystika.tarot.reading.Reading.Chapter;
import com.mystika.tarot.spreads.Spread;
import com.mystika.tarot.spreads.ThreeCardSpread;

@Component
class Seer {

    public Reading basicReading(Spread spread) {
        return switch (spread) {
            case ThreeCardSpread(String _, String _, List<DrawnCard> cards) ->
                new Reading(List.of(
                    new Chapter("Past", "In your past, the %s card \"%s\" suggests that you have recently experienced significant events that have shaped your current situation: %s."
                        .formatted(cards.getFirst().orientation() == DrawnCard.Orientation.REVERSED ? "reversed " : "",
                            cards.getFirst().name(), cards.getFirst().meaning())),
                    new Chapter("Present", "Looking at your present, the %s card \"%s\" indicates that you are currently facing important decisions or challenges that require your attention: %s."
                        .formatted(cards.get(1).orientation() == DrawnCard.Orientation.REVERSED ? "reversed " : "",
                            cards.get(1).name(), cards.get(1).meaning())),
                    new Chapter("Future", "As for your future, the %s card \"%s\" hints at upcoming opportunities or changes that may affect your path ahead: %s."
                        .formatted(cards.getLast().orientation() == DrawnCard.Orientation.REVERSED ? "reversed " : "",
                            cards.getLast().name(), cards.getLast().meaning())
                )));
        };
    }

    public Reading aiReading() {
        // TODO
        return new Reading(List.of());
    }

}
