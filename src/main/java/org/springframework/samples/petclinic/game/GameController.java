package org.springframework.samples.petclinic.game;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.board.BoardService;
import org.springframework.samples.petclinic.boardcard.BoardCard;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  @author Ricardo Nadal Garcia 
 *  @author Jose Maria Delgado Sanchez
 */

 @Controller
 @RequestMapping("/games")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CardService cardService;

    @GetMapping()
    public String gameList(ModelMap modelMap){
        String view ="games/gamesList";
        Iterable<Game> games= gameService.findAll();
        modelMap.addAttribute("games", games);
        return view;
    }

    @GetMapping("/{gameId}/players")
    public String gamePlayers(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view="games/playersList";
        Iterable<Player> players= gameService.findPlayerList(gameId);
        Game game=gameService.findGameById(gameId);
        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        return view;
    }

    @GetMapping("/{gameId}/roll")
    public String gameRoll(ModelMap modelMap, @PathVariable("gameId") int gameId){
        String view ="games/roll";

        Iterable<Player> players= gameService.findPlayerList(gameId);
        Game game=gameService.findGameById(gameId);
        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);

        Roll roll=new Roll();
        
        modelMap.addAttribute("roll",roll);

        //Retrieve data from board_card association and generate a list of cards
        List<BoardCard> boardCard = boardService.findCardList(game.getBoard().getId());
        Collection<Card> cards = boardCard.stream()
                                            .filter(x -> x.getSold() == false)
                                            .map(x -> cardService.findCardById(x.getCard().getId()))
                                            .collect(Collectors.toSet());
        modelMap.addAttribute("cards", cards);

        return view;
    }

    @PostMapping("/{gameId}/roll")
    public String rollKeep(@ModelAttribute("roll") Roll roll,BindingResult result,ModelMap modelMap, @PathVariable("gameId") int gameId) throws DuplicatedMonsterNameException  {
        
        String view="games/roll";
        gameService.turnRoll(roll);
        if(roll.getRollAmount()==roll.getMaxThrows()) {
            Integer playerIdActualTurn=1;
            playerService.useRoll(gameId,playerIdActualTurn,roll);
        }
        


        Game game=gameService.findGameById(gameId);
        Iterable<Player> players= gameService.findPlayerList(gameId);
        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        modelMap.addAttribute("roll",roll);

        return view;
    }
    

        

        
        
       
}
