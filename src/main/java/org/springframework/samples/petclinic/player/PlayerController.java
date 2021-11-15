package org.springframework.samples.petclinic.player;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Ricardo Nadal Garcia
 */

@Controller
@RequestMapping("/players")
public class PlayerController {
    
    @Autowired
    private PlayerService playerService;


    @GetMapping()
    public String cardsList(ModelMap modelMap){
        String view ="players/playersList";
        Iterable<Player> players= playerService.findAll();
        modelMap.addAttribute("players", players);
        return view;
    }

    @GetMapping("/{playerId}/roll")
    public String roll(ModelMap modelMap, @PathVariable("playerId") int playerId) {
        String view="players/roll";

        Player player=playerService.findPlayerById(playerId);
        modelMap.put("player", player);

        
        
        List<DiceValues> dices=playerService.turnRoll(0,player.keep);
        modelMap.addAttribute("dices",dices);

        modelMap.addAttribute("rollAmount",0);

        return view;
    }

    @PostMapping("/{playerId}/roll")
    public String rollKeep(@Valid Player player,BindingResult result,ModelMap modelMap, @PathVariable("playerId") int playerId,@RequestParam("rollAmount") Integer rollAmount) throws DuplicatedMonsterNameException {
        
        String view="players/roll";
        rollAmount++;
        
        List<DiceValues> dices=playerService.turnRoll(rollAmount,player.keep);
        modelMap.addAttribute("dices",dices);
        
        Player playerRolled=playerService.findPlayerById(playerId);

        
        modelMap.put("player", playerRolled);
        
        modelMap.addAttribute("rollAmount",rollAmount);
        return view;
    }
}
