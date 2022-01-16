package org.springframework.samples.kingoftokyo.player;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rosa Molina
 * @author Sara Cruz
*/

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {

    /**
	 * Retrieve list of monsters.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT p.monster FROM Player p")
    List<Integer> listMonster() throws DataAccessException;

}
