package org.springframework.samples.petclinic.game;
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
	 * Retrieve all <code>PetType</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Game</code>s
	 */
	@Query("SELECT game FROM Game game WHERE game.winner IS NULL")
	List<Game> findOnGoingGames() throws DataAccessException;
    
}
