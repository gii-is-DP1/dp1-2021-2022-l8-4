package org.springframework.samples.petclinic.player;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
/**
 * @author Noelia López Durán
 */
public interface PlayerStatusRepository extends Repository<PlayerStatus,Integer>{
    /**
	 * Save a <code>playerStatus</code> to the data store, either inserting or updating it.
	 * @param playerStatus the <code>playerStatus</code> to save
	 * @see BaseEntity#isNew
	 */
    void save(PlayerStatus playerStatus) throws DataAccessException;

	List<PlayerStatus> findByPlayerId(Integer playerId);
}
