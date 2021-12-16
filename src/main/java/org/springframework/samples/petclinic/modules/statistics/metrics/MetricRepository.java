package org.springframework.samples.petclinic.modules.statistics.metrics;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.game.Game;

public interface MetricRepository extends CrudRepository<Game, Integer> {
    
     
    /**
	 * Retrieve finished games count grouped by user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT player.user.id AS user, COUNT(player) AS score FROM Player player WHERE player.game.winner IS NOT NULL GROUP BY player.user.id ORDER BY count(player) DESC")
    List<Long[]> gamesPlayed() throws DataAccessException;
}
