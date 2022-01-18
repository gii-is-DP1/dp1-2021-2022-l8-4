package org.springframework.samples.kingoftokyo.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 * @author Carlos Varela Soult
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
 
public class CardServiceTest {

    private static final Integer NUM_CARTAS = 30;
    private CardService cardService;
    
    @Autowired
    private CardServiceTest(CardService cardService) {
        this.cardService = cardService;
    }

    @Test
    void testFindCardTypes() {
        Collection<CardType> cardTypes = cardService.findCardTypes();
        List<CardType> cardTypesList = cardTypes.stream().collect(Collectors.toList());
        CardType cardType1 = cardTypesList.get(0);
        assertThat(cardType1).isEqualTo(CardType.DESCARTAR);
        CardType cardType2 = cardTypesList.get(1);
        assertThat(cardType2).isEqualTo(CardType.PERMANENTE);
    }

    @Test
    @Transactional
    void shouldFindAll() {
        List<Card> listcont = new ArrayList<>();
        cardService.findAll().forEach(listcont::add);
        assertEquals(listcont.size(), NUM_CARTAS);

        Card newCard = new Card();
        newCard.setId(50);
        newCard.setCardEnum(CardEnum.NATIONALGUARD);
        newCard.setCost(10);
        newCard.setType(CardType.PERMANENTE);
        cardService.saveCard(newCard);

        List<Card> listcont2 = new ArrayList<>();
        cardService.findAll().forEach(listcont2::add);
        assertEquals(listcont2.size(), NUM_CARTAS+1);
    }

    @Test
    void shouldFindCardById() {
        Card card = cardService.findCardById(10);
        Card anotherCard = cardService.findCardById(25);
        assertEquals(card.getCost(), 4);
        assertEquals(anotherCard.getCost(), 7);
        assertThat(card.getCardEnum().getName()).startsWith("Bombardeo");
        assertThat(anotherCard.getCardEnum().getName()).startsWith("¡Tiene");
    }

    @Test
    @Transactional
    void shouldSaveCardIntoDatabaseAndGenerateId() {
        Card newCard = new Card();
        newCard.setId(88);
        newCard.setCardEnum(CardEnum.CAMOUFLAGE);
        newCard.setCost(36);
        newCard.setType(CardType.PERMANENTE);
        cardService.saveCard(newCard);
        assertThat(newCard.getId()).isNotNull();
    }

    @Test
    @Transactional
	void shouldUpdateCardName() {
		Card card = cardService.findCardById(1);
		String newName = "Fábrica de agua";
        String newDescription = "Llueve y moja al jugador siguiente, así que pierde su turno";
        card.getCardEnum().setName(newName);
        card.getCardEnum().setDescription(newDescription);
		this.cardService.saveCard(card);

		card = this.cardService.findCardById(1);
		assertThat(card.getCardEnum().getName()).isEqualTo(newName);
        assertThat(card.getCardEnum().getDescription()).isEqualTo(newDescription);
	}

    @Test
    void shouldCardCount() {
        int count = cardService.cardCount();
        assertEquals(count,NUM_CARTAS);
    }

}
