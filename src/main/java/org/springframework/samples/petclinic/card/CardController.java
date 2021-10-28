package org.springframework.samples.petclinic.card;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

    private static final String VIEWS_CARDS_CREATE_OR_UPDATE_FORM = "cards/createOrUpdateCardForm";

    @GetMapping()
    public String cardsList(ModelMap modelMap){
        String view ="cards/cardsList";
        Iterable<Card> cards= cardService.findAll();
        modelMap.addAttribute("cards", cards);
        return view;
    }

    @GetMapping(path = "/new")
    public String initCreationForm(ModelMap modelMap) {
        String view = VIEWS_CARDS_CREATE_OR_UPDATE_FORM;
        modelMap.addAttribute("card", new Card());
        List<CardType> types = new ArrayList<CardType>();
        types.add(CardType.PERMANENTE);types.add(CardType.DESCARTAR);
        modelMap.addAttribute("types", types );
        return view;
    }

    @PostMapping(path = "/new")
    public String processCreationForm(@Valid Card card, BindingResult result, ModelMap modelMap) {
        String view="cards/cardsList";
        if (result.hasErrors()){

            modelMap.addAttribute("card",card);
            return VIEWS_CARDS_CREATE_OR_UPDATE_FORM;

        }else {
            //creating card
            cardService.saveCard(card);
            modelMap.addAttribute("message","Card succesfully saved!");
            view = cardsList(modelMap);
        }
        return view;
    } 
}
