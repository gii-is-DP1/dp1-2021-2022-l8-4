package org.springframework.samples.petclinic.card;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    
	@ModelAttribute("cardTypes")
	public Collection<CardType> populateCardTypes() {
		return this.cardService.findCardTypes();
	}

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

    @GetMapping(value = "/{cardId}/edit")
	public String initUpdateForm(@PathVariable("cardId") int cardId, ModelMap modelMap) {
		Card card = this.cardService.findCardById(cardId);
		modelMap.put("card", card);
		return VIEWS_CARDS_CREATE_OR_UPDATE_FORM;
	}

    /**
     *
     * @param card
     * @param result
     * @param cardId
     * @param model
     * @return
     */
    @PostMapping(value = "/{cardId}/edit")
	public String processUpdateForm(@Valid Card card, BindingResult result, @PathVariable("cardId") int cardId, ModelMap modelMap) {
		if (result.hasErrors()) {
			modelMap.put("card", card);
			return VIEWS_CARDS_CREATE_OR_UPDATE_FORM;
		}
		else {
                        Card cardToUpdate=this.cardService.findCardById(cardId);
			BeanUtils.copyProperties(card, cardToUpdate, "id");                                                                                                    
                    this.cardService.saveCard(cardToUpdate);                    
			return "redirect:/cards";
		}
	}

}
