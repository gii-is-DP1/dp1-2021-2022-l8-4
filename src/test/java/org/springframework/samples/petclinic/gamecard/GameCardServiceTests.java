package org.springframework.samples.petclinic.gamecard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.CardType;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamecard.GameCardService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
/**
 * @author Carlos Varela Soult
 * @author Rosa Molina
 */

public class GameCardServiceTests {
    
    @Autowired
    private GameCardService gameCardService;

    @Autowired
    private GameService gameService;

    @Autowired
    private CardService cardService;

    @Test
    public void shouldFindGameCardWithCorrectId() {
        Game game = gameService.findGameById(1);
        Card card = cardService.findCardById(1);
        GameCard gameCard = gameCardService.findByGameCard(game, card);
        assertThat(gameCard.getGame().getId()).isEqualTo(game.getId());
        assertThat(gameCard.getCard().getId()).isEqualTo(card.getId());
    }

    @Test
    public void shouldInsertGameCard() {
        GameCard gameCard = new GameCard();
        gameCard.setSold(true);

        Card card = new Card();
        card.setName("Fábrica de huesos");
        card.setCost(7);
        card.setType(CardType.PERMANENTE);
        Game game = new Game();
        //game.setTokyoCityStatus(LocationStatus.LIBRE);
        //game.setTokyoBayStatus(LocationStatus.LIBRE);
        game = gameService.findGameById(1);
        
        gameCard.setCard(card);
        gameCard.setGame(game);
        gameCardService.saveGameCard(gameCard);
        assertThat(gameCard.getId()).isNotNull();
        assertThat(gameCard.getCard().getId()).isNotNull();
        assertThat(gameCard.getGame().getId()).isNotNull();
        assertThat(gameCard.getCard().getName()).startsWith("Fábrica");
    }

    @Test
    public void shouldUpdateGameCard() {
        Game oldGame = gameService.findGameById(1);
        Card oldCard = cardService.findCardById(1);
        GameCard GameCard = gameCardService.findByGameCard(oldGame, oldCard);
        GameCard.setGame(gameService.findGameById(2));
        GameCard.setCard(cardService.findCardById(1));
        GameCard.setSold(true);
        assertThat(GameCard.getGame()).isNotEqualTo(oldGame);
        assertThat(GameCard.getCard()).isEqualTo(oldCard);
        assertTrue(GameCard.getSold());
    }

    @Test
    public void shouldFindAvailableCardsByGame() throws Exception {
        Game game = gameService.findGameById(1);
        List<Card> availableCardsSet = gameCardService.findAvailableCardsByGame(game);
        List<Card> availableCardsList = availableCardsSet.stream().collect(Collectors.toList());
        assertThat(availableCardsList.get(0).getId()).isNotNull();
        assertThat(availableCardsList.get(0).getName()).startsWith("Monstruo");
    }

}
