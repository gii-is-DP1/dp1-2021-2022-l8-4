package org.springframework.samples.petclinic.card;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Controller
public class CardController {
    
    private static final String VIEWS_CARD_CREATE_OR_UPDATE_FORM = "cards/createOrUpdateCardForm";

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService){
        this.cardService = cardService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/cards/new")
    public String initCreationForm(Map<String, Object> model) {
        Card card = new Card();
        model.put("card", card);
        return VIEWS_CARD_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/cards/new")
    public String processCreationForm(@Valid Card card, BindingResult result) {
        if (result.hasErrors()){
            return VIEWS_CARD_CREATE_OR_UPDATE_FORM;
        }else {
            //creating card
            this.cardService.saveCard(card);

            return "redirect:/cards/" + card.getId();
        }
    }

    @GetMapping(value = "/cards")
    public String processFindForm(Card card, BindingResult result, Map<String, Object> model) {

        Collection<Card> results = this.cardService.findAll();
        if(results.isEmpty()){
            //no cards found
            result.rejectValue("notFound", "not found");
            return "cards";
        }else {
            model.put("selections", results);
            return "cards/cardsList";
        }
    }
}
