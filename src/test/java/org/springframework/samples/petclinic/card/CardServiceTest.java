package org.springframework.samples.petclinic.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.exceptions.DuplicatedCardNameException;
import org.springframework.samples.petclinic.deck.Deck;
import org.springframework.samples.petclinic.deck.DeckService;
import org.springframework.stereotype.Service;

/**
 * @author Carlos Varela Soult
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
 
public class CardServiceTest {
    
    @Autowired
    private CardService cardService;
    @Autowired
    private DeckService deckService;

    @Test
    public void testFindCardTypes() {
        Collection<CardType> cardTypes = cardService.findCardTypes();
        List<CardType> cardTypesList = cardTypes.stream().collect(Collectors.toList());
        CardType cardType1 = cardTypesList.get(0);
        assertThat(cardType1).isEqualTo(CardType.DESCARTAR);
        CardType cardType2 = cardTypesList.get(1);
        assertThat(cardType2).isEqualTo(CardType.PERMANENTE);
    }

    @Test
    public void testFindAll() {
        Iterable<Card> all = cardService.findAll();
        // Iterating over all cards
        for (Card card:all) {
            String monsterName = card.getName();
            monsterName.equals("Monstruo Alfa");
        }

    }

    @Test
    public void testCountWithInitialData(){
        int count = cardService.cardCount();
        assertEquals(count, 1);
    }

    @Test
    public void testSaveCardIntoDatabaseAndGenerateId() {
        Deck deck = deckService.findDeckById(1);
        Card card = new Card();
        card.setName("Fábrica de lava");
        card.setCost(5);
        card.setDiscarded(false);
        card.setType(CardType.DESCARTAR);
        card.setDeck(deck);
        cardService.saveCard(card);
        assertThat(card.getDeck()).isEqualTo(deck);
        assertThat(card.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void testThrowExceptionUsingCardsWithTheSameName() {
        Deck deck = deckService.findDeckById(1);
        Card card = new Card();
        card.setName("Fábrica de lava");
        card.setCost(5);
        card.setDiscarded(false);
        card.setType(CardType.DESCARTAR);
        card.setDeck(deck);
        try {
            this.cardService.saveCard(card);;
        } catch (DuplicatedCardNameException ex) {
            ex.printStackTrace();
        }

        Card anotherCardWithTheSameName = new Card();
        anotherCardWithTheSameName.setName("Fábrica de lava");
        anotherCardWithTheSameName.setCost(20);
        anotherCardWithTheSameName.setDiscarded(true);
        anotherCardWithTheSameName.setType(CardType.PERMANENTE);
        anotherCardWithTheSameName.setDeck(deck);
        Assertions.assertThrows(DuplicatedCardNameException.class, () ->{
			cardService.saveCard(anotherCardWithTheSameName);
		});
    }

    @Test
    @Transactional
	public void testUpdateCardName() throws Exception {
		Card card = cardService.findCardById(1);

		String newName = "Fábrica de agua";
		card.setName(newName);
		this.cardService.saveCard(card);

		card = this.cardService.findCardById(1);
		assertThat(card.getName()).isEqualTo(newName);
	}

    @Test
    public void testFindCardById() {
        Card card = cardService.findCardById(1);
        assertEquals(card.getCost(), 5);
        assertThat(card.getName()).startsWith("Monstruo Alfa");
        assertThat(card.getDiscarded()).isEqualTo(false);
    }

}
