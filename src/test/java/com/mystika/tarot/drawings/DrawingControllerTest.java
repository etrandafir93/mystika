package com.mystika.tarot.drawings;

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
class DrawingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void threeCardSpread() throws Exception {
        mockMvc.perform(post("/api/drawings/three-card-spread").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": "test-drawing-123",
                            "deckSlug": "major-arcana"
                        }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("test-drawing-123"))
            .andExpect(jsonPath("$.deckSlug").value("major-arcana"))
            .andExpect(jsonPath("$.cards").isArray())
            .andExpect(jsonPath("$.cards.length()").value(3))
            .andExpect(jsonPath("$.cards[*].position").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].cardSlug").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].orientation").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].meaning").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].imageUrl").isNotEmpty());
    }

}
