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
	 * Retrieve won games count grouped by user.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT new org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData(player.user, COUNT(player)) FROM Player player WHERE player.game.winner IS player.user.username GROUP BY player.user.id ORDER BY count(player) DESC")
    Page<MetricData> scoresRanking(Pageable pageable) throws DataAccessException;

    /**
	 * Retrieve won games count grouped by user.
	 * @return a <code>Integer</code>
	 */
    //SELECT USER_ID, SUM(REWARD_POINTS ) FROM USERS_ACHIEVEMENTS INNER JOIN ACHIEVEMENTS ON USERS_ACHIEVEMENTS.ACHIEVEMENT_ID  = ACHIEVEMENTS.ID GROUP BY USER_ID ;
    //@Query("SELECT ua.user_id, SUM(a.rewardPoints) FROM users_achievements ua INNER JOIN achievements a on ua.achievement_id = a.id GROUP BY ua.user_id")
    //List<Object> scoresRanking() throws DataAccessException;

    /**
	 * Retrieve number of games.
	 * @return a <code>Integer</code>
	 */
   @Query("SELECT COUNT(g) FROM Game g")
   Integer totalGamesOfApp() throws DataAccessException;

    /**
	 * Retrieve number of games for login user.
	 * @return a <code>Integer</code>
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
   
   



}
