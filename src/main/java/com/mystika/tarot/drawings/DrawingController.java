package com.mystika.tarot.drawings;

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

    private final Seer seer;

    @PostMapping("/three-card-spread")
    Drawing threeCardSpread(@RequestBody DrawingRequest dawReq) {
        log.info("three-card-spread requested: {}", dawReq);
        var drawResp = seer.draw(new ThreeCardSpread(dawReq.id, dawReq.deckSlug));
        log.info("three-card-spread generated: {}", drawResp);
        return drawResp;
    }

    record DrawingRequest(@RequestParam String id, String deckSlug) {
    }
}
