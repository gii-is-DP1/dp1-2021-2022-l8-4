package org.springframework.samples.petclinic.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.boardcard.BoardCardService;
import org.springframework.samples.petclinic.card.Card;

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
    private BoardCardService boardCardService;

    @GetMapping()
    public String gameListNotFinished(ModelMap modelMap){
        String view ="games/gamesList";
        Iterable<Game> games= gameService.findAllNotFinished();
        modelMap.addAttribute("games", games);
        return view;
    }

    @GetMapping("/finished")
    public String gameListFinished(ModelMap modelMap){
        String view ="games/gamesListFinished";
        Iterable<Game> games= gameService.findAllFinished();
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


        if(MapGameRepository.getInstance().getTurnList(gameId) == null) {
            List<Integer> turnList=gameService.initialTurnList(gameId);
            MapGameRepository.getInstance().putTurnList(gameId, turnList);
        }
       
        List<Integer> turnList=MapGameRepository.getInstance().getTurnList(gameId);
        Roll roll=MapGameRepository.getInstance().getRoll(gameId);

        String actualPlayerTurn=gameService.actualTurn(gameId).getMonsterName().toString();
        modelMap.addAttribute("actualPlayerTurn",actualPlayerTurn);
        Boolean isPlayerTurn=gameService.isPlayerTurn(gameId);
        modelMap.addAttribute("isPlayerTurn",isPlayerTurn);
        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        modelMap.addAttribute("roll",roll);
        //Retrieve data from board_card association and generate a list of cards
        Set<Card> cards = boardCardService.findAvailableCardsByBoard(game.getBoard());
        modelMap.addAttribute("cards", cards);
        modelMap.addAttribute("turnList",turnList);

        return view;
    }

    @PostMapping("/{gameId}/roll")
    public String rollKeep(@ModelAttribute("newTurn") Boolean nuevoTurno,@ModelAttribute("roll") Roll roll,BindingResult result,ModelMap modelMap, @PathVariable("gameId") int gameId) throws DuplicatedMonsterNameException  {
        if(gameService.isPlayerTurn(gameId)) {
            if(nuevoTurno){
                gameService.nuevoTurno(gameId);
            } else{
                gameService.turnRoll(roll,gameId);
                if(roll.getRollAmount()==roll.getMaxThrows()) {
                    Integer playerIdActualTurn=gameService.actualTurnPlayerId(gameId);
                    playerService.useRoll(gameId,playerIdActualTurn,roll);
                    
                }
            }
        }
        
        return "redirect:/games/{gameId}/roll";
    }
    

        

        
        
       
}
