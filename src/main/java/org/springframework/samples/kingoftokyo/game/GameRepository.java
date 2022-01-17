package org.springframework.samples.kingoftokyo.game;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA GameRepository interface
 * 
 * @author Jose Maria Delgado Sanchez
 */
@Repository
public interface GameRepository extends CrudRepository<Game, Integer>{

    /**
	 * Retrieve not finished <code>Game</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Game</code>s
	 */
	@Query("SELECT game FROM Game game WHERE game.winner IS NULL AND turn > 0")
	List<Game> findOnGoingGames() throws DataAccessException;

	/**
	 * Retrieve lobbies <code>Game</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Game</code>s
	 */
	@Query("SELECT game FROM Game game WHERE game.turn IS 0")
	List<Game> findLobbies() throws DataAccessException;

    
}
