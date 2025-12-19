package com.mystika.tarot.readings;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mystika.tarot.drawings.Drawing;
import com.mystika.tarot.drawings.DrawnCard;
import com.mystika.tarot.drawings.ThreeCardSpread;
import com.mystika.tarot.readings.Reading.Chapter;

@Component
public class Seer {

    public Reading read(Drawing drawing) {
        return switch (drawing) {
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

}
