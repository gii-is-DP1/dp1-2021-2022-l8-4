package org.springframework.samples.petclinic.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.deck.Deck;
import org.springframework.stereotype.Service;

/**
 * @author Carlos Varela Soult
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
 
public class CardServiceTest {
    
    @Autowired
    private CardService cardService;
    private CardRepository cardRepository;

    @Test
    public void testCountWithInitialData(){
        int count = cardService.cardCount();
        assertEquals(count, 1);
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
    public void testSaveCard() {
        Card card = new Card();
        Deck deck = new Deck();
        List<Card> cardList = new ArrayList<>();
        
        // Creating a new card
        card.setId(66);
        card.setName("Fábrica de lava");
        card.setCost(4);
        card.setDiscarded(true);
        card.setType(CardType.DESCARTAR);
        
        // Creating a new deck
        deck.setId(1);
        // Set deckId to card
        card.setDeck(deck);
        // Set card to deck cardList
        cardList.add(card);
        deck.setCardList(cardList);
        cardService.saveCard(card); 
    }

    @Test
    public void testFindCardById() {
        Card card = cardService.findCardById(1);
        assertEquals(card.getCost(), 5);
    }

}
