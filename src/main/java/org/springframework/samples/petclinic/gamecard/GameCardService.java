package org.springframework.samples.petclinic.gamecard;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class GameCardService {
    @Autowired
    private GameCardRepository gameCardRepository;

    @Transactional
    public Set<Card> findAvailableCardsByGame(Game game){
        return game.getGameCard()
                    .stream()
                    .filter(x -> x.getSold() == false)
                    .map(x -> x.getCard())
                    .collect(Collectors.toSet());
    }

    @Transactional
    public void saveBoardCard(GameCard gameCard) throws DataAccessException{
        gameCardRepository.save(gameCard);
    }

    @Transactional
    public GameCard findByGameCard(Game game, Card card) throws DataAccessException {
        return gameCardRepository.findByGameCards(game, card);
    }


}
