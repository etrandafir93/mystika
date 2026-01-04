package com.mystika.tarot.cards;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
class RaiderWaiteDeckTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper json;

    @ParameterizedTest
    @ValueSource(strings = { " ", "rider-waite" })
    void shouldReturnDefaultDeck_whenDeckNameIsDefault(String deckName) throws Exception {
        mvc.perform(get("/api/decks/" + deckName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.slug").value("rider-waite"))
            .andExpect(jsonPath("$.name").value("Rider-Waite"))
            .andExpect(jsonPath("$.cards").isArray())
            .andExpect(jsonPath("$.cards").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].name").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].meaning").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].reversedMeaning").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].symbols").isNotEmpty())
            .andExpect(jsonPath("$.cards[*].imageUrl").isNotEmpty());
    }

    @TestFactory
    Stream<DynamicNode> deckShouldBeCorrect() throws Exception {
        String response = mvc.perform(get("/api/decks/rider-waite"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<TarotCard> allCards = json.readValue(response, TarotDeck.class)
            .cards();

        return Stream.of(
            dynamicTest("has 78 cards", () -> assertThat(allCards.size() == 78)),
            dynamicTest("has 78 names", () -> hasDifferentObjects(allCards, 78, TarotCard::name)),
            dynamicTest("has 78 slugs", () -> hasDifferentObjects(allCards, 78, TarotCard::slug)),
            dynamicTest("has 78 images", () -> hasDifferentObjects(allCards, 78, TarotCard::imageUrl)),
            dynamicTest("has 78 meanings", () -> hasDifferentObjects(allCards, 78, TarotCard::meaning)),

            dynamicContainer("major-arcana", Stream.of(
                dynamicTest("has 22 cards", () -> hasSimilarObjects(allCards, 22,
                    it -> it.suite().equals("major-arcana"))),
                dynamicTest("has 22 detailed meanings for love", () -> hasSimilarObjects(
                    filterBySuite(allCards, "major-arcana"), 22, it -> it.detailedMeaning().love() != null)),
                dynamicTest("has 22 detailed meanings for career", () -> hasSimilarObjects(
                    filterBySuite(allCards, "major-arcana"), 22, it -> it.detailedMeaning().career() != null)),
                dynamicTest("has 22 detailed meanings for spirituality", () -> hasSimilarObjects(
                    filterBySuite(allCards, "major-arcana"), 22, it -> it.detailedMeaning().spirituality() != null))
            )),
            dynamicContainer("cups", Stream.of(
                    dynamicTest("has 14 cards", () -> hasSimilarObjects(allCards, 14,
                        it -> it.suite().equals("cups"))),
                    dynamicTest("has 14 detailed meanings for love", () -> hasSimilarObjects(
                        filterBySuite(allCards, "cups"), 14, it -> it.detailedMeaning().love() != null)),
                    dynamicTest("has 14 detailed meanings for career", () -> hasSimilarObjects(
                        filterBySuite(allCards, "cups"), 14, it -> it.detailedMeaning().career() != null)),
                    dynamicTest("has 14 detailed meanings for spirituality", () -> hasSimilarObjects(
                        filterBySuite(allCards, "cups"), 14, it -> it.detailedMeaning().spirituality() != null))
            )),
            dynamicContainer("swords", Stream.of(
                dynamicTest("has 14 cards", () -> hasSimilarObjects(allCards, 14,
                    it -> it.suite().equals("swords"))),
                dynamicTest("has 14 detailed meanings for love", () -> hasSimilarObjects(
                    filterBySuite(allCards, "swords"), 14, it -> it.detailedMeaning().love() != null)),
                dynamicTest("has 14 detailed meanings for career", () -> hasSimilarObjects(
                    filterBySuite(allCards, "swords"), 14, it -> it.detailedMeaning().career() != null)),
                dynamicTest("has 14 detailed meanings for spirituality", () -> hasSimilarObjects(
                    filterBySuite(allCards, "swords"), 14, it -> it.detailedMeaning().spirituality() != null))
            )),
            dynamicContainer("pentacles", Stream.of(
                dynamicTest("has 14 pentacles", () -> hasSimilarObjects(allCards, 14,
                    it -> it.suite().equals("pentacles"))),
                dynamicTest("has 14 detailed meanings for love", () -> hasSimilarObjects(
                    filterBySuite(allCards, "pentacles"), 14, it -> it.detailedMeaning().love() != null)),
                dynamicTest("has 14 detailed meanings for career", () -> hasSimilarObjects(
                    filterBySuite(allCards, "pentacles"), 14, it -> it.detailedMeaning().career() != null)),
                dynamicTest("has 14 detailed meanings for spirituality", () -> hasSimilarObjects(
                    filterBySuite(allCards, "pentacles"), 14, it -> it.detailedMeaning().spirituality() != null))
            )),
            dynamicContainer("wands", Stream.of(
                dynamicTest("has 14 wands", () -> hasSimilarObjects(allCards, 14,
                    it -> it.suite().equals("wands"))),
                dynamicTest("has 14 detailed meanings for love", () -> hasSimilarObjects(
                    filterBySuite(allCards, "wands"), 14, it -> it.detailedMeaning().love() != null)),
                dynamicTest("has 14 detailed meanings for career", () -> hasSimilarObjects(
                    filterBySuite(allCards, "wands"), 14, it -> it.detailedMeaning().career() != null)),
                dynamicTest("has 14 detailed meanings for spirituality", () -> hasSimilarObjects(
                    filterBySuite(allCards, "wands"), 14, it -> it.detailedMeaning().spirituality() != null))
            ))
        );
    }

    private static List<TarotCard> filterBySuite(List<TarotCard> allCards, String suite) {
        return allCards.stream()
            .filter(c -> c.suite()
                .equals(suite))
            .toList();
    }

    private static void hasSimilarObjects(List<TarotCard> allCards, int expectedCount, Predicate<TarotCard> property) {
        assertThat(allCards)
            .filteredOn(property)
            .hasSize(expectedCount);
    }

    private static void hasDifferentObjects(List<TarotCard> allCards, int expectedCount, Function<TarotCard, Object> property) {
        Stream<Object> distinctObjects = allCards.stream()
            .map(property)
            .distinct();
        assertThat(distinctObjects)
            .hasSize(expectedCount);
    }

    @Test
    void shouldReturnNotFound_whenDeckNameIsNotDefault() throws Exception {
        mvc.perform(get("/api/decks/custom-cards"))
            .andExpect(status().isNotFound());
    }

}
