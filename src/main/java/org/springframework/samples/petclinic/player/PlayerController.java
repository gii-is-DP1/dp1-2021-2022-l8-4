package org.springframework.samples.petclinic.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.playercard.PlayerCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Ricardo Nadal Garcia
 * @author Jose Maria Delgado Sanchez
 */

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
        Card card = cardService.findCardById(cardId);
        playerCardService.buyCard(player, card);
        return "redirect:/games/" + player.getGame().getId() + "/playing";
    }

    @GetMapping("/{playerId}/cards/discard")
    public String discardAllCards(ModelMap modelMap, @PathVariable("playerId") int playerId) {
        Player player = playerService.findPlayerById(playerId);
        playerCardService.discardShopCards(player);
        return "redirect:/games/" + player.getGame().getId() + "/playing";
    }

    @GetMapping("/{playerId}/surrender")
    public String surrender(ModelMap modelMap, @PathVariable("playerId") int playerId) {
        playerService.surrender(playerId);
        return "redirect:/";
    }

}
