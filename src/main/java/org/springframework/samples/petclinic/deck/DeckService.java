package org.springframework.samples.petclinic.deck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class DeckService {
    
    @Autowired
    private DeckRepository deckRepository;

    @Transactional
    public Iterable<Deck> findAll(){
        Iterable<Deck> res = deckRepository.findAll();
        return res;
    }

    @Transactional
    public int deckCount(){
        return (int) deckRepository.count();
    }

    @Transactional
    public void saveDeck(Deck deck){
        deckRepository.save(deck);
    }

    @Transactional
    public Deck findDeckById(int id) throws DataAccessException{
        return deckRepository.findById(id).get();
    }
}
