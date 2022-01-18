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

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerCardService playerCardService;

    @Autowired
    private CardService cardService;

    @GetMapping()
    public String cardsList(ModelMap modelMap) {
        String view = "players/playersList";
        Iterable<Player> players = playerService.findAll();
        modelMap.addAttribute("players", players);
        return view;
    }

    @GetMapping("/{playerId}/cards/{cardId}/buy")
    public String buyCard(ModelMap modelMap, @PathVariable("playerId") int playerId,
            @PathVariable("cardId") int cardId) {
        Player player = playerService.findPlayerById(playerId);
    
        try{
            Card card = cardService.findCardById(cardId);
            playerCardService.buyCard(player, card);
        }catch(NotFoundException e){
            log.warn(e.toString());
        }catch(InvalidPlayerActionException e){
            log.warn(e.toString());
        }
        
        return "redirect:/games/" + player.getGame().getId() + "/playing";
    }

    @GetMapping("/{playerId}/cards/discard")
    public String discardAllCards(ModelMap modelMap, @PathVariable("playerId") int playerId) {
        Player player = playerService.findPlayerById(playerId);
        
        try{
            playerCardService.discardShopCards(player);
        }catch(InvalidPlayerActionException e){
            log.warn(e.toString());
        }
        
        return "redirect:/games/" + player.getGame().getId() + "/playing";
    }

    @GetMapping("/{playerId}/surrender")
    public String surrender(ModelMap modelMap, @PathVariable("playerId") int playerId) {
        playerService.surrender(playerId);
        return "redirect:/";
    }

}
