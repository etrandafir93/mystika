package com.mystika.tarot.reading;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/drawings")
class DrawingController {

    private final Seeker seeker;
    private final Seer seer;

    @PostMapping("/three-card-spread")
    Response threeCardSpread(@RequestBody DrawingRequest dawReq) {
        log.info("three-card-spread requested: {}", dawReq);

        var draw = seeker.draw(new ThreeCardSpread(dawReq.id, dawReq.deckSlug));
        log.info("three-card-spread generated: {}", draw);

        var reading = seer.basicReading(draw);
        log.info("reading generated: {}", reading);

        return new Response(draw,reading);
    }

    record Response(Drawing drawing, Reading reading) {}

    record DrawingRequest(@RequestParam String id, String deckSlug) {
    }
}
