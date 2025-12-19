package com.mystika.tarot.reading;

import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        var reading = seer.basicReading(draw);
        log.info("reading generated: {}", reading);

        return new Response(draw, reading);
    }

    private Drawing spread(Request req) {
        return switch (req.drawingType) {
            case THREE_CARD_SPREAD -> new ThreeCardSpread(req.id, req.deckSlug);
        };
    }

    record Response(Drawing drawing, Reading reading) {

    }

    record Request(@RequestParam String id, String deckSlug, DrawingType drawingType) {

        enum DrawingType {
            THREE_CARD_SPREAD
        }
    }
}
