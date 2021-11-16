package org.springframework.samples.petclinic.game;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  @author Ricardo Nadal Garcia 
 */

 @Controller
 @RequestMapping("/games")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @GetMapping()
    public String gameList(ModelMap modelMap){
        String view ="games/gamesList";
        Iterable<Game> games= gameService.findAll();
        modelMap.addAttribute("games", games);
        return view;
    }

    @GetMapping("/{gameId}/roll")
    public String gameRoll(ModelMap modelMap){
        String view ="games/roll";

        Roll roll=new Roll();
        
        modelMap.addAttribute("roll",roll);
       

        return view;
    }

    @PostMapping("/{gameId}/roll")
    public String rollKeep(@Valid Roll roll,BindingResult result,ModelMap modelMap, @PathVariable("gameId") int gameId,@RequestParam("rollAmount") Integer rollAmount)  {
        
        String view="games/roll";

        roll.setMaxThrows(3);
        roll.setRollAmount(rollAmount);
        gameService.turnRoll(roll);
        
        
        
        modelMap.addAttribute("roll",roll);

        return view;
    }
    

        

        
        
       
}
