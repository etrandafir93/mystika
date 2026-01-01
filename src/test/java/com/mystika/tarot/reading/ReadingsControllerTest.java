package com.mystika.tarot.reading;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReadingsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void threeCardSpread() throws Exception {
        mvc.perform(post("/api/readings").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": "test-spread-123",
                            "deckSlug": "rider-waite",
                            "drawingType": "THREE_CARD_SPREAD"
                        }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.spread.id").value("test-spread-123"))
            .andExpect(jsonPath("$.spread.deckSlug").value("rider-waite"))
            .andExpect(jsonPath("$.spread.cards").isArray())
            .andExpect(jsonPath("$.spread.cards.length()").value(3))
            .andExpect(jsonPath("$.spread.cards[*].position").isNotEmpty())
            .andExpect(jsonPath("$.spread.cards[*].cardSlug").isNotEmpty())
            .andExpect(jsonPath("$.spread.cards[*].orientation").isNotEmpty())
            .andExpect(jsonPath("$.spread.cards[*].meaning").isNotEmpty())
            .andExpect(jsonPath("$.spread.cards[*].imageUrl").isNotEmpty())
            .andExpect(jsonPath("$.reading.chapters").isArray())
            .andExpect(jsonPath("$.reading.chapters.length()").value(3))
            .andExpect(jsonPath("$.reading.chapters[*].title").isNotEmpty())
            .andExpect(jsonPath("$.reading.chapters[*].content").isNotEmpty());
    }

}
