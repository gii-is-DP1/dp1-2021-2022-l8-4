package org.springframework.samples.petclinic.card;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jose Maria Delgado Sanchez
 * @author Noelia López Durán
 * @author Ricardo Nadal García
 */
@Controller
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    

    @GetMapping()
    public String cardsList(ModelMap modelMap){
        String view ="cards/cardsList";
        Iterable<Card> cards= cardService.findAll();
        modelMap.addAttribute("cards", cards);
        return view;
    }

    @GetMapping(path = "/new")
    public String initCreationForm(ModelMap modelMap) {
        String view = "cards/editCard";
        modelMap.addAttribute("card", new Card());
        return view;
    }

    @PostMapping(path = "/save")
    public String processCreationForm(@Valid Card card, BindingResult result, ModelMap modelMap) {
        String view="cards/cardsList";
        if (result.hasErrors()){

            modelMap.addAttribute("card",card);
            return "cards/editCard";

        }else {
            //creating card
            cardService.saveCard(card);
            modelMap.addAttribute("message","Card succesfully saved!");
            
        }
        return view;
    } 
/*
    @GetMapping(value = "/cards")
    public String processFindForm(Card card, BindingResult result, Map<String, Object> model) {

        Iterable<Card> results = this.cardService.findAll();
        if(results.){
            //no cards found
            result.rejectValue("notFound", "not found");
            return "cards";
        }else {
            model.put("selections", results);
            return "cards/cardsList";
        }
    }*/
}
