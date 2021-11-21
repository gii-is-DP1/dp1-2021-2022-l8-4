package org.springframework.samples.petclinic.game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;
import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.dice.RollValuesFormatter;
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
 */

 @Controller
 @RequestMapping("/games")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

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

      //  if(roll.equals(null) || rollAmount.equals(null)) {
           // roll=new Roll();
     //   } 
         Roll roll=new Roll();

        List<Integer> turnList=new ArrayList<Integer>();
      //  if(turnListText.equals(null) ||turnListText.isEmpty()) {
            turnList=game.initialTurnList();
       // }else{
    //    turnList=ListTurnFormatter.stringToList(turnListText);
      //  }
        
        
        

        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        modelMap.addAttribute("roll",roll);
        
        modelMap.addAttribute("turnList",turnList);

        return view;
    }

    @PostMapping("/{gameId}/roll")
    public String rollKeep(@ModelAttribute("turnList") String turnListText,@ModelAttribute("newTurn") Boolean nuevoTurno,@ModelAttribute("roll") Roll roll,BindingResult result,ModelMap modelMap, @PathVariable("gameId") int gameId) throws DuplicatedMonsterNameException  {
        List<Integer> turnList=ListTurnFormatter.stringToList(turnListText);
        String view="games/roll";
        gameService.turnRoll(roll);
        if(roll.getRollAmount()==roll.getMaxThrows()) {
            Integer playerIdActualTurn=gameService.actualTurnPlayerId(turnList, gameId);
            playerService.useRoll(gameId,playerIdActualTurn,roll);
        }
        
        if(nuevoTurno){
            gameService.nuevoTurno(gameId);
            roll=new Roll();
        }

        Game game=gameService.findGameById(gameId);
        Iterable<Player> players= gameService.findPlayerList(gameId);
        modelMap.addAttribute("turnList",turnList);
        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        modelMap.addAttribute("roll",roll);

        return view;
    }
    

        

        
        
       
}
