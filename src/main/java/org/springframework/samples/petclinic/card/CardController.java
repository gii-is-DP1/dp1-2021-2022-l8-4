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
 */
@Controller
@RequestMapping("/cards")
public class CardController {
    
   private static final String VIEWS_CARD_CREATE_OR_UPDATE_FORM = "cards/createOrUpdateCardForm";

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
        String view = VIEWS_CARD_CREATE_OR_UPDATE_FORM;
        modelMap.addAttribute("card", new Card());
        return view;
    }

    @PostMapping(path = "/new")
    public String processCreationForm(@Valid Card card, BindingResult result) {
        if (result.hasErrors()){
            return VIEWS_CARD_CREATE_OR_UPDATE_FORM;
        }else {
            //creating card
            this.cardService.saveCard(card);

            return "redirect:/cards/" + card.getId();
        }
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
