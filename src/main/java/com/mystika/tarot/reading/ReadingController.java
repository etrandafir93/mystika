package com.mystika.tarot.reading;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mystika.tarot.cards.TarotDecksRepository;
import com.mystika.tarot.spreads.Spread;
import com.mystika.tarot.spreads.SpreadFocus;
import com.mystika.tarot.spreads.ThreeCardSpread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/readings")
class ReadingController {

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

}
