package org.springframework.samples.kingoftokyo.playercard;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.card.CardService;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
/**
 * @author Rosa Molina
 */

public class PlayerCardServiceTests {
    
    @Autowired
    private PlayerCardService playerCardService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CardService cardService;

    @Test
    public void shouldFindPlayerCard() {
        Player player = playerService.findPlayerById(1);
        Card card = cardService.findCardById(1);
        PlayerCard playercard = new PlayerCard();
        playercard.setCard(card);
        playercard.setPlayer(player);
        playercard.setDiscarded(Boolean.FALSE);
        playerCardService.savePlayerCard(playercard);
        assertEquals(playercard.getCard(), card);
        assertEquals(playercard.getPlayer(), player);
        assertEquals(playercard.getDiscarded(), Boolean.FALSE);
    }

    @Test
    public void shouldUsePlayerCard() {
        Player player = playerService.findPlayerById(1);
        Card card = cardService.findCardById(1);
        playerCardService.buyCard(player, card);
    }

}
