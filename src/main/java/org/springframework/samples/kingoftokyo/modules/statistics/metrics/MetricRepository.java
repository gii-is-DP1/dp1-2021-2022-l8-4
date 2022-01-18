package org.springframework.samples.kingoftokyo.modules.statistics.metrics;


import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.user.User;

/**
*  @author Rosa Molina
*  @author Sara Cruz
*/


public interface MetricRepository extends CrudRepository<Game, Integer> {
    
     
    /**
	 * Retrieve finished games count grouped by user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT new org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData(player.user, COUNT(player)) FROM Player player WHERE player.game.winner IS NOT NULL GROUP BY player.user.id ORDER BY count(player) DESC")
    Page<MetricData> gamesPlayedRanking(Pageable pageable) throws DataAccessException;

    /**
	 * Retrieve won games count grouped by user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT new org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData(player.user, COUNT(player)) FROM Player player WHERE player.game.winner IS player.user.username GROUP BY player.user.id ORDER BY count(player) DESC")
    Page<MetricData> winsRanking(Pageable pageable) throws DataAccessException;

    /**
	 * Retrieve cards used count grouped by user.
	 * @return a <code>Integer</code>
     * CONSULTA NO FUNCIONA COMO DEBERIA- MIRAR MAÑANA CON SARA
     */
    @Query("SELECT new org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData(player.user, COUNT(pc.id)) FROM Player player JOIN PlayerCard pc WHERE player.id is pc.player.id GROUP BY player.user.id ORDER BY count(pc.id) DESC")
    Page<MetricData> cardsRanking(Pageable pageable) throws DataAccessException;


    /**
	 * Retrieve max scores grouped by user.
	 * @return a <code>Pageable</code>
	 */
    @Query("SELECT new org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData(user, SUM(achiev.rewardPoints)) FROM User user INNER JOIN user.achievements achiev INNER JOIN achiev.users userAchiev WHERE user.id IS userAchiev.id GROUP BY user.id ORDER BY sum(achiev.rewardPoints) DESC")
    Page<MetricData> scoresRanking(Pageable pageable) throws DataAccessException;

    /**
	 * Retrieve number of games.
	 * @return a <code>Integer</code>
	 */
   @Query("SELECT COUNT(g) FROM Game g")
   Integer totalGamesOfApp() throws DataAccessException;

    /**
	 * Retrieve number of games for login user.
	 * @return a <code>List<Game></code>
	 */
    @Query("SELECT p.game FROM Player p WHERE p.user =?1 AND p.game.winner IS NOT NULL")
    List<Game> findGamesCurrentUser(User user) throws DataAccessException;
    
    /**
	 * Retrieve number of games winning for login user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT COUNT(g) FROM Game g WHERE g.winner =?1")
    Integer totalGamesWinnerCurrentUser(String username) throws DataAccessException;

    /**
	 * Retrieve number of games for login user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT COUNT(p.game) FROM Player p WHERE p.user =?1 AND p.game.winner IS NOT NULL")
    Integer totalGamesCurrentUser(User user) throws DataAccessException;
   
    /**
	 * Retrieve list of users with max turns in tokyo
	 * @return a <code>Integer</code>
	 */

    @Query("SELECT new org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData(user, user.maxTurnsTokyo) FROM User user ORDER BY user.maxTurnsTokyo desc")
    Page<MetricData> maxTurnUsers(Pageable pageable) throws DataAccessException;



}
