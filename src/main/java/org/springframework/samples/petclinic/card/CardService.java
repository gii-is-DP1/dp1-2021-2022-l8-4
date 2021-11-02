package org.springframework.samples.petclinic.card;

import java.util.ArrayList;
import java.util.Collection;

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

    @Transactional 
    public Iterable<Card> findAll(){
        Iterable<Card> res = cardRepository.findAll();
        return res;
    }
    @Transactional(readOnly = true)
	public Collection<CardType> findCardTypes() throws DataAccessException {
        Collection<CardType> ct = new ArrayList<CardType>();
        ct.add(CardType.DESCARTAR);ct.add(CardType.PERMANENTE);
		return ct;
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
