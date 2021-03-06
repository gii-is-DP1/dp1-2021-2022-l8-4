package org.springframework.samples.kingoftokyo.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.card.CardService;
import org.springframework.samples.kingoftokyo.player.exceptions.InvalidPlayerActionException;
import org.springframework.samples.kingoftokyo.playercard.PlayerCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ricardo Nadal Garcia
 * @author Jose Maria Delgado Sanchez
 */
@Slf4j
@Controller
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService;
    private PlayerCardService playerCardService;
    private CardService cardService;
    private static final String VIEWS_EXCEPTION = "exception";

    @Autowired
    public PlayerController(PlayerService playerService, 
            PlayerCardService playerCardService, 
            CardService cardService) {
        this.playerService = playerService;
        this.playerCardService = playerCardService;
        this.cardService = cardService;
    }

    @GetMapping("/{playerId}/cards/{cardId}/buy")
    public String buyCard(ModelMap modelMap, @PathVariable("playerId") int playerId,
            @PathVariable("cardId") int cardId) {

        try {
            Player player = playerService.findPlayerById(playerId);
            Card card = cardService.findCardById(cardId);
            playerCardService.buyCard(player, card);
            return "redirect:/games/" + player.getGame().getId() + "/playing";
        } catch (NotFoundException | InvalidPlayerActionException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }
    }

    @GetMapping("/{playerId}/cards/discard")
    public String discardAllCards(ModelMap modelMap, @PathVariable("playerId") int playerId) {

        try {
            Player player = playerService.findPlayerById(playerId);
            playerCardService.discardShopCards(player);
            return "redirect:/games/" + player.getGame().getId() + "/playing";
        } catch (NotFoundException | InvalidPlayerActionException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }
    }

    @GetMapping("/{playerId}/surrender")
    public String surrender(ModelMap modelMap, @PathVariable("playerId") int playerId) {
        try {
            Player player = playerService.findPlayerById(playerId);
            playerService.surrender(player);
            return "redirect:/games/" + player.getGame().getId() + "/playing";
        } catch (NotFoundException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }
    }

}
