package org.springframework.samples.petclinic.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Iterators;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

/** 
 *@author Noelia López Durán
 */
public class PlayerServiceTests {
    
    @Autowired
    protected PlayerService playerService;
    @Autowired
	protected GameService gameService;	


    @Test
    public void testCountWithInitialData(){
        int count = playerService.playerCount();
       assertEquals(count,4);
    }
    @Test
    public void testFindPlayerWithCorrectId(){
        Player player4 = playerService.findPlayerById(4);
        assertThat(player4.getMonsterName().toString()).startsWith("CyberBunny");
		assertThat(player4.getLifePoints()).isEqualTo(4);
        assertThat(player4.getVictoryPoints()).isEqualTo(8);
        assertThat(player4.getEnergyPoints()).isEqualTo(2);
        assertThat(player4.getLocation().toString()).startsWith("bahiaTokyo");
        assertThat(player4.getGame().getId()).isEqualTo(1);
    }
    @Test
	@Transactional
	public void shouldInsertPlayerIntoDatabaseAndGenerateId() {
        Game game1 = this.gameService.findGameById(1);
		Player player = new Player();
		player.setMonsterName(MonsterName.GigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.fueraTokyo);
        player.setGame(game1);
            try {
                this.playerService.savePlayer(player);;
            } catch (DuplicatedMonsterNameException ex) {
                Logger.getLogger(PlayerServiceTests.class.getName()).log(Level.SEVERE, null, ex);
            }
		// checks that id has been generated
		assertThat(player.getId()).isNotNull();
	}

    @Test
	@Transactional
	public void shouldThrowExceptionInsertingPlayersWithTheSameName(){
        Game game1 = this.gameService.findGameById(1);
		Player player = new Player();
		player.setMonsterName(MonsterName.GigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.fueraTokyo);
        player.setGame(game1);
            try {
                this.playerService.savePlayer(player);;
            } catch (DuplicatedMonsterNameException ex) {
                ex.printStackTrace();
            }

        Player anotherPlayerWithTheSameName = new Player();		
		anotherPlayerWithTheSameName.setMonsterName(MonsterName.GigaZaur);
		anotherPlayerWithTheSameName.setGame(game1);
        anotherPlayerWithTheSameName.setLifePoints(10);
        anotherPlayerWithTheSameName.setVictoryPoints(0);
        anotherPlayerWithTheSameName.setEnergyPoints(0);
        anotherPlayerWithTheSameName.setLocation(LocationType.fueraTokyo);
		Assertions.assertThrows(DuplicatedMonsterNameException.class, () ->{
			playerService.savePlayer(anotherPlayerWithTheSameName);
		});	

	}
    @Test
	@Transactional
	public void shouldUpdateMonsterName() throws Exception {
		Player player2 = this.playerService.findPlayerById(2);

		MonsterName newName = MonsterName.Alien;
		player2.setMonsterName(newName);
		this.playerService.savePlayer(player2);

		player2 = this.playerService.findPlayerById(2);
		assertThat(player2.getMonsterName()).isEqualTo(newName);
	}

}
