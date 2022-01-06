package org.springframework.samples.petclinic.playercard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.AssertDelegateTarget;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.card.CardEnum;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.CardType;
import org.springframework.samples.petclinic.card.Deck;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.player.PlayerServiceTests;
import org.springframework.samples.petclinic.playercard.PlayerCard;
import org.springframework.samples.petclinic.playercard.PlayerCardService;
import org.springframework.samples.petclinic.player.PlayerService;
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
