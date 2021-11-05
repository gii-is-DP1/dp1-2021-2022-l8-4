package org.springframework.samples.petclinic.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional //(readOnly = true)
    public Iterable<Card> findAll(){
        Iterable<Card> res = cardRepository.findAll();
        return res;
    }

    @Transactional
    public int cardCount(){
        return (int) cardRepository.count();
    }

    @Transactional
    public void saveCard(Card card)  {
        //creating card
        cardRepository.save(card);
    }

    @Transactional
	public Card findCardById(int id) throws DataAccessException {
		return cardRepository.findById(id).get();
	}
    
}
