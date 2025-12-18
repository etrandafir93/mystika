package com.mystika.tarot.cards;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TarotDeckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = { " ", "major-arcana" })
    void shouldReturnDefaultDeck_whenDeckNameIsDefault(String deckName) throws Exception {
        mockMvc.perform(get("/api/decks/" + deckName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.slug").value("major-arcana"))
            .andExpect(jsonPath("$.name").value("Major Arcana"))
            .andExpect(jsonPath("$.cards").isArray())
            .andExpect(jsonPath("$.cards").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].name").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].meaning").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].reversedMeaning").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].symbols").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].imageUrl").isNotEmpty());
    }

    @Test
    void shouldReturnNotFound_whenDeckNameIsNotDefault() throws Exception {
        mockMvc.perform(get("/api/decks/custom-cards"))
            .andExpect(status().isNotFound());
    }

}
