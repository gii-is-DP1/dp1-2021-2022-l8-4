package org.springframework.samples.petclinic.player;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/roll")
    public String roll(ModelMap modelMap) {
        String view="players/roll";
        List<DiceValues> dices=playerService.roll();
        modelMap.addAttribute("dices",dices);
        return view;
    }
}
