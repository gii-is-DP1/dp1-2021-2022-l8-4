package org.springframework.samples.petclinic.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.j2objc.annotations.AutoreleasePool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.boardcard.BoardCardService;
import org.springframework.samples.petclinic.card.Card;

import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
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

    @Autowired
    private UserService userService;

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

    @GetMapping("/{gameId}/roll") //PREGUNTAR AL PROFESOR RESPECTO QUE HACER CON EL TEMA DE ROLL, COMO LO OBTENGO AQUI PARA LOS DEMAS JUGADORES 
    public String gameRoll(ModelMap modelMap, @PathVariable("gameId") int gameId){
        String view ="games/roll";

        Iterable<Player> players= gameService.findPlayerList(gameId);
        Game game=gameService.findGameById(gameId);

        if(MapGameRepository.getInstance().getTurnList(gameId) == null) {
            List<Integer> turnList=game.initialTurnList();
            MapGameRepository.getInstance().putTurnList(gameId, turnList);
        }
       

        
        
        List<Integer> turnList=MapGameRepository.getInstance().getTurnList(gameId);
        Roll roll=MapGameRepository.getInstance().getRoll(gameId);

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
        
        List<Integer> turnList=MapGameRepository.getInstance().getTurnList(gameId);
        

        if(nuevoTurno){
            gameService.nuevoTurno(gameId);
            roll=new Roll();

        } else{
            gameService.turnRoll(roll);
            if(roll.getRollAmount()==roll.getMaxThrows()) {
                Integer playerIdActualTurn=gameService.actualTurnPlayerId(turnList, gameId);
                playerService.useRoll(gameId,playerIdActualTurn,roll);
            }
        }

        MapGameRepository.getInstance().putRoll(gameId,roll);
       

        return "redirect:/games/{gameId}/roll";
    }
    
    @GetMapping("/new") 
    public String newGame(ModelMap modelMap){
        if(userService.authenticatedUser() instanceof User){
            String view ="games/newGame";
            modelMap.addAttribute("newGame", new Game());
            return view;
        }else{
            return "redirect:/";
        }
    }
        
    @PostMapping("/new")
    public String createNewGame(@ModelAttribute("newGame") Game newGame, ModelMap modelMap){
        if(userService.authenticatedUser() instanceof User){
            User user = userService.authenticatedUser();
            gameService.createNewGame(user, newGame);
            
            return "games/" + newGame.getId() + "/lobby";
        }else{
            return "redirect:/";
        }
    }
        
        
       
}
