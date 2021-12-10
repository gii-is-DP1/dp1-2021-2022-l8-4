package org.springframework.samples.petclinic.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Disabled;
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
    
    @Autowired
    private CardService cardService;

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
    @Disabled
    public void testFindAll() {
        Iterable<Card> all = cardService.findAll();
        // Iterating over all cards
        for (Card card:all) {
            String monsterName = card.getCardEnum().getName();
            monsterName.equals("Monstruo Alfa");
        }

    }

    @Test
    @Disabled //Esto se deja asi hasta que se añadan todas las cartas y se cambie el numero de cartas por el que haya en dicho momento
    public void testCountWithInitialData(){
        int count = cardService.cardCount();
        assertEquals(count, 10);
    }

    @Test
    @Disabled
    public void testSaveCardIntoDatabaseAndGenerateId() {
        Card card = new Card();
       // card.setName("Fábrica de lava");
        card.setCost(5);
        card.setType(CardType.DESCARTAR);
        cardService.saveCard(card);
        assertThat(card.getId()).isNotNull();
    }

    @Test
    @Disabled
    @Transactional
	public void testUpdateCardName() throws Exception {
		Card card = cardService.findCardById(1);

		String newName = "Fábrica de agua";
	//	card.getCardEnum().setName(newName);
		this.cardService.saveCard(card);

		card = this.cardService.findCardById(1);
	//	assertThat(card.getName()).isEqualTo(newName);
	}

    @Test
    @Disabled
    public void testFindCardById() {
        Card card = cardService.findCardById(1);
        assertEquals(card.getCost(), 5);
        assertThat(card.getCardEnum().getName()).startsWith("Monstruo Alfa");
    }

}
