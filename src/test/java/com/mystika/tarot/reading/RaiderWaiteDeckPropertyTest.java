package com.mystika.tarot.reading;

import static org.assertj.core.api.Assertions.assertThat;

import com.mystika.tarot.cards.TarotDecksRepository;
import com.mystika.tarot.reading.ReadingController.Request.DrawingType;
import com.mystika.tarot.spreads.SpreadFocus;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import tools.jackson.databind.json.JsonMapper;

class RaiderWaiteDeckPropertyTest {

    private ReadingController reading = new ReadingController(
        new Seer(),
        new Seeker(
            new TarotDecksRepository(JsonMapper.builder().build())
        )
    );

    @Property
    void raiderWaiteDeck(
        @ForAll String readingId,
        @ForAll DrawingType drawingType,
        @ForAll SpreadFocus drawingFocus
    ) {
        var request = new ReadingController.Request(
            readingId,
            TarotDecksRepository.RIDER_WAITE,
            drawingType,
            drawingFocus
        );

        var response = reading.reading(request);

        response.reading()
            .chapters()
            .forEach(chapter ->
                assertThat(chapter)
                    .hasFieldOrProperty("title")
                    .hasFieldOrProperty("content")
            );

        response.spread()
            .cards()
            .forEach(card ->
                assertThat(card)
                    .hasFieldOrProperty("name")
                    .hasFieldOrProperty("meaning")
                    .hasFieldOrProperty("cardMeaning")
                    .hasFieldOrProperty("symbols")
                    .hasFieldOrProperty("imageUrl")
                    .hasFieldOrProperty("orientation")
            );
    }

}
