package org.springframework.samples.petclinic.card;


import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class CardService {
    
    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional(readOnly = true)
    public Collection<Card> findAll() throws DataAccessException {
        return StreamSupport.stream(cardRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList()); 
    }

    @Transactional
    public void saveCard(Card card) throws DataAccessException {
        //creating card
        cardRepository.save(card);
    }
}
