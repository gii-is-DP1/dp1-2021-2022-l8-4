package org.springframework.samples.kingoftokyo.playercard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.card.CardService;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.game.GameService;
import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.samples.kingoftokyo.gamecard.GameCard;
import org.springframework.samples.kingoftokyo.gamecard.GameCardService;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.player.exceptions.InvalidPlayerActionException;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
/**
 * @author Rosa Molina
 * @author Jose Maria Delgado Sanchez
 */
class PlayerCardServiceTests {

    @MockBean
    private UserService userService;

    @Autowired
    private PlayerCardService playerCardService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CardService cardService;

    @Autowired
    private GameCardService gameCardService;

    @Autowired
    private GameService gameService;

    @Autowired
    private MapGameRepository mapGameRepository;

    private Player player3;
    private Game game1;
    private Card card1;

    @BeforeEach
    void configure() throws NotFoundException  {
        //Turno de player id=3 en el game id=1 con user id=3 y rollAmount=3
        this.player3 = playerService.findPlayerById(3);
        this.game1 = gameService.findGameById(1);
        this.card1 = cardService.findCardById(1);

        User user = new User();
        user.setId(3);

        Mockito.when(userService.authenticatedUser()).thenReturn(user);
        Mockito.when(userService.isAuthUserPlayingAsPlayer(any(Player.class))).thenReturn(Boolean.TRUE);
    }

    @Test
    void shouldFindPlayerCard() throws NotFoundException  {
        Player player = playerService.findPlayerById(1);
        Card card = cardService.findCardById(1);
        PlayerCard playercard = new PlayerCard();
        playercard.setCard(card);
        playercard.setPlayer(player);
        playercard.setDiscarded(Boolean.FALSE);
        playerCardService.savePlayerCard(playercard);

        assertEquals(card, playercard.getCard());
        assertEquals(player, playercard.getPlayer());
        assertEquals(Boolean.FALSE, playercard.getDiscarded());
    }

    @Test
    void shouldBuyCard() throws InvalidPlayerActionException, NotFoundException {

        Integer energy = player3.getEnergyPoints();

        GameCard gameCard = gameCardService.findByGameCard(game1, card1);
        assertFalse(gameCard.getSold(), "La carta no debe haber sido marcada como comprada");

        playerCardService.buyCard(player3, card1);

        GameCard gameCardSold = gameCardService.findByGameCard(game1, card1);
        PlayerCard playerCard = playerCardService.findByPlayerCard(player3, card1);

        assertTrue(gameCardSold.getSold(), "La carta debe haber sido marcada como vendida");
        assertNotNull(playerCard, "La carta debe haber sido comprada por el player con id=3");
        assertEquals(energy - card1.getCost(), player3.getEnergyPoints(),
                "Debe haber restado el coste de la carta a los puntos de energia del player");
    }

    @Test
    void buyCardNotEnoughEnergy() throws InvalidPlayerActionException {

        player3.setEnergyPoints(0);

        assertThrows(InvalidPlayerActionException.class, () -> {
            playerCardService.buyCard(player3, card1);
        }, "Debe lanzarse la excepcion InvalidPlayerActionException");
    }

    @Test
    void buyCardAvailableCardsFail() throws InvalidPlayerActionException {

        GameCard gameCard = gameCardService.findByGameCard(game1, card1);
        gameCard.setSold(Boolean.TRUE);

        assertThrows(InvalidPlayerActionException.class, () -> {
            playerCardService.buyCard(player3, card1);
        }, "Debe lanzarse la excepcion InvalidPlayerActionException");
    }

    @Test
    void buyCardIsOnGoingFail() throws InvalidPlayerActionException {

        game1.setTurn(0);

        assertThrows(InvalidPlayerActionException.class, () -> {
            playerCardService.buyCard(player3, card1);
        }, "Debe lanzarse la excepcion InvalidPlayerActionException");
    }

    @Test
    void buyCardIsDeadFail() throws InvalidPlayerActionException {

        player3.setLifePoints(0);

        assertThrows(InvalidPlayerActionException.class, () -> {
            playerCardService.buyCard(player3, card1);
        }, "Debe lanzarse la excepcion InvalidPlayerActionException");
    }

    @Test
    void buyCardRollFinishedFail() throws InvalidPlayerActionException {
        // Inicializa el roll para que rollAmount valga 0
        mapGameRepository.putRoll(game1.getId(), new Roll());

        assertThrows(InvalidPlayerActionException.class, () -> {
            playerCardService.buyCard(player3, card1);
        }, "Debe lanzarse la excepcion InvalidPlayerActionException");
    }

    @Test
    void shouldDiscardShopCards() throws InvalidPlayerActionException, NotFoundException{
        List<Card> cardList = gameCardService.findAvailableCardsByGame(game1);
        Set<Integer> ids = cardList.stream()
                                .map(c->c.getId())
                                .collect(Collectors.toSet());
        Set<Integer> expectedIds = new HashSet<>(Arrays.asList(1, 2, 3));
        Integer energy = player3.getEnergyPoints();
        Integer cost = 2;

        playerCardService.discardShopCards(player3);

        List<Card> newCardList = gameCardService.findAvailableCardsByGame(game1);
        Boolean oldCardFound = newCardList.stream()
                                        .filter(c -> ids.contains(c.getId()))
                                        .findAny()
                                        .isPresent();

        assertEquals(expectedIds, ids, "Las cartas 1,2 y 3 deben estar en la tienda antes de descartar");
        assertEquals(energy-cost, player3.getEnergyPoints(), "La accion le ha debido costar al jugador 2 de energia");
        assertFalse(oldCardFound, "Todas las deben haber sido descartadas");
    }


    @Test
    void discardShopCardsNotEnoughEnergy() throws InvalidPlayerActionException {

        player3.setEnergyPoints(0);

        assertThrows(InvalidPlayerActionException.class, () -> {
            playerCardService.discardShopCards(player3);
        }, "Debe lanzarse la excepcion InvalidPlayerActionException");
    }

    @Test
    void discardShopCardsIsDeadFail() throws InvalidPlayerActionException {

        player3.setLifePoints(0);

        assertThrows(InvalidPlayerActionException.class, () -> {
            playerCardService.discardShopCards(player3);
        }, "Debe lanzarse la excepcion InvalidPlayerActionException");
    }

    @Test
    void useCardsWhenBuyNoEffect(){
        Integer energy = player3.getEnergyPoints();
        Integer cost = card1.getCost();

        Integer newEnergy = playerCardService.useCardsWhenBuy(player3, energy, cost);

        assertEquals(energy, newEnergy, "La energia debe coincidir ya que no tiene ninguna carta que modique el comportamiento");
    }

    @Test
    void useCardsWhenBuyDifferentEnergy() throws NotFoundException {
        Integer energy = player3.getEnergyPoints();
        Integer cost = card1.getCost();
        Card newCard = cardService.findCardById(26);
        PlayerCard newPlayerCard = new PlayerCard(player3,newCard);
        playerCardService.savePlayerCard(newPlayerCard);

        Integer newEnergy = playerCardService.useCardsWhenBuy(player3, energy, cost);

        assertNotEquals(energy, newEnergy, "La energia debe no coincidir ya que tiene una carta que modifica el coste");
    }

}
