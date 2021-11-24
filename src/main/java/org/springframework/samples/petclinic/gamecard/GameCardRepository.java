package org.springframework.samples.petclinic.gamecard;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCardRepository extends CrudRepository<GameCard,Integer>{
    
    /**
	 * Retrieve <code>GameCard</code>s from the data store.
	 * @return a <code>GameCard</code> of <code>Game</code>s
	 */
	@Query("SELECT gc FROM GameCard gc WHERE gc.game = ?1 AND gc.card = ?2")
	GameCard findByGameCards(Game game, Card card) throws DataAccessException;
}
