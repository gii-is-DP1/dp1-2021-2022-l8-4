package org.springframework.samples.petclinic.card;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.MapGameRepository;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jose Maria Delgado Sanchez
 * @author Noelia López Durán
 */
@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional // (readOnly = true)
    public Iterable<Card> findAll() {
        Iterable<Card> res = cardRepository.findAll();
        return res;
    }

    @Transactional(readOnly = true)
    public Collection<CardType> findCardTypes() throws DataAccessException {
        Collection<CardType> ct = new ArrayList<CardType>();
        ct.add(CardType.DESCARTAR);
        ct.add(CardType.PERMANENTE);
        return ct;
    }

    @Transactional
    public int cardCount() {
        return (int) cardRepository.count();
    }

    @Transactional
    public void saveCard(Card card) {
        // creating card
        cardRepository.save(card);
    }

    @Transactional
    public Card findCardById(int id) throws DataAccessException {
        return cardRepository.findById(id).get();
    }

    /**
     * Initialize a new deck in a game
     * 
     * @return Deck randomized
     */
    @Transactional
    public void newDeck(Game game) {
        Deck deck = new Deck(findAll());
        MapGameRepository.getInstance().putDeck(game, deck);
    }

    /**
     * Next card in the game's deck
     * 
     * @param game linked to the deck
     * @return Card from the deck if there are left
     */
    @Transactional
    public Card nextCard(Game game) {
        Deck deck = MapGameRepository.getInstance().getDeck(game);
        Card card = deck.nextCard();
        MapGameRepository.getInstance().putDeck(game, deck);
        return card;
    }

}
