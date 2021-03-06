package org.springframework.samples.kingoftokyo.modules.statistics.achievement;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
*  @author Jose Maria Delgado
*  @author Rosa Molina
*  @author Sara Cruz
*/

 @Repository
 public interface AchievementRepository extends CrudRepository<Achievement, Integer>{
     
    /**
	 * Retrieve number of finished games by User.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT COUNT(player) FROM Player player WHERE player.user.id=?1 AND player.game.winner IS NOT NULL")
    Integer gamesPlayedByUser(int userId) throws DataAccessException;

    /**
	 * Retrieve number of wins by User.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT COUNT(player) FROM Player player WHERE player.user.id=?1 AND player.game.winner = player.user.username")
    Integer winsByUser(int userId) throws DataAccessException;

    /**
	 * Retrieve number of bought cards by User.
	 * @return a <code>Integer</code>
	 */
    @Query("SELECT COUNT(pc) FROM PlayerCard pc WHERE player.user.id=?1 AND pc.discarded IS TRUE")
    Integer cardsBoughtByUser(int userId) throws DataAccessException;

   /**
	 * Retrieve number of turns by User.
	 * @return a <code>Integer</code>
	 */
   @Query("SELECT u.maxTurnsTokyo FROM User u WHERE u.id=?1")
   Integer turnsByUser(int userId) throws DataAccessException;
 }