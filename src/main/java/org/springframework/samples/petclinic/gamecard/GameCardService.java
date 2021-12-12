package org.springframework.samples.petclinic.gamecard;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.Deck;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.MapGameRepository;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class GameCardService {

    @Autowired
    private GameCardRepository gameCardRepository;

    @Autowired
    private CardService cardService;

    @Transactional
    public Iterable<GameCard> findAll() {
        return gameCardRepository.findAll();
    }

    @Transactional
    public void saveGameCard(GameCard gameCard) throws DataAccessException {
        gameCardRepository.save(gameCard);
    }

    /**
     * Find available cards to buy in a game
     * 
     * @param game
     * @return List of cards available to buy in a game
     */
    @Transactional
    public List<Card> findAvailableCardsByGame(Game game) {
        return StreamSupport.stream(findAll().spliterator(), Boolean.FALSE)
                .filter(x -> x.getSold() == Boolean.FALSE && x.getGame().getId() == game.getId()).map(x -> x.getCard())
                .collect(Collectors.toList());
    }

    /**
     * Retrieve GameCard association given a card and a game from the data store.
     * 
     * @param game
     * @param card
     * @return GameCard object associated
     */
    @Transactional
    public GameCard findByGameCard(Game game, Card card) throws DataAccessException {
        return gameCardRepository.findByGameCards(game.getId(), card.getId());
    }

    /**
     * Always shows 3 cards in the game's shop if there are cards left in the deck
     * 
     * @param game
     */
    @Transactional
    public void showCards(Game game) {
        Integer maxCardsOnSale = 3;
        Integer availableCards = findAvailableCardsByGame(game).size();
        if (availableCards < maxCardsOnSale) {
            Deck deck = MapGameRepository.getInstance().getDeck(game);
            if (deck != null) {
                if (!deck.isEmpty()) {
                    Card card = cardService.nextCard(game);
                    GameCard gameCard = new GameCard();
                    gameCard.setCard(card);
                    gameCard.setGame(game);
                    gameCard.setSold(false);
                    saveGameCard(gameCard);

                    // Recursive call
                    showCards(game);
                }
            }
        }
    }

    

}
