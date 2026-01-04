package com.mystika.tarot.reading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mystika.tarot.cards.TarotDecksRepository;
import com.mystika.tarot.spreads.Spread;
import com.mystika.tarot.spreads.SpreadFocus;
import com.mystika.tarot.spreads.ThreeCardSpread;

@RestController
@RequestMapping("api/readings")
class ReadingController {

    private static final Logger log = LoggerFactory.getLogger(ReadingController.class);

    private final Seer seer;
    private final Seeker seeker;

    @PostMapping
    Response reading(@RequestBody Request req) {
        log.info("reading requested: {}", req);

        var draw = seeker.draw(spread(req));
        log.info("drawing generated: {}", draw);

        var reading = seer.basicReading(draw, req.focus);
        log.info("reading generated: {}", reading);

        return new Response(draw, reading);
    }

    private Spread spread(Request req) {
        return switch (req.drawingType) {
            case THREE_CARD_SPREAD -> new ThreeCardSpread(req.id, req.deckSlug);
        };
    }

    record Response(Spread spread, Reading reading) {

    }

    record Request(String id, String deckSlug, DrawingType drawingType, SpreadFocus focus) {

        Request {
            if (deckSlug == null || deckSlug.isBlank()) {
                deckSlug = TarotDecksRepository.RIDER_WAITE;
            }
            if (focus == null) {
                focus = SpreadFocus.LOVE;
            }
        }

        enum DrawingType {
            THREE_CARD_SPREAD
        }
    }

    ReadingController(Seer seer, Seeker seeker) {
        this.seer = seer;
        this.seeker = seeker;
    }
}
