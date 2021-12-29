package org.springframework.samples.petclinic.modules.statistics.metrics;


import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.game.Game;

public interface MetricRepository extends CrudRepository<Game, Integer> {
    
     
    /**
	 * Retrieve finished games count grouped by user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT new org.springframework.samples.petclinic.modules.statistics.metrics.MetricData(player.user, COUNT(player)) FROM Player player WHERE player.game.winner IS NOT NULL GROUP BY player.user.id ORDER BY count(player) DESC")
    Page<MetricData> gamesPlayedRanking(Pageable pageable) throws DataAccessException;

    /**
	 * Retrieve won games count grouped by user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT new org.springframework.samples.petclinic.modules.statistics.metrics.MetricData(player.user, COUNT(player)) FROM Player player WHERE player.game.winner IS player.user.username GROUP BY player.user.id ORDER BY count(player) DESC")
    Page<MetricData> winsRanking(Pageable pageable) throws DataAccessException;
}
