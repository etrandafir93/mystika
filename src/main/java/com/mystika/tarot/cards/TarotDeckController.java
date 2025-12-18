package com.mystika.tarot.cards;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/decks")
class TarotDeckController {

    private final TarotDecksRepository tarotDecks;

    @GetMapping("/{slug}")
    ResponseEntity<TarotDeck> deck(@Nullable @PathVariable(value = "slug", required = false) String slug) {
        slug = StringUtils.isBlank(slug) ? "rider-waite" : slug;
        return ResponseEntity.of(tarotDecks.bySlug(slug));
    }

}
