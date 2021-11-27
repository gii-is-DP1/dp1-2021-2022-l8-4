package org.springframework.samples.petclinic.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

/** 
 *@author Noelia López Durán
 *@author Ricardo Nadal Garcia
 */
public class PlayerServiceTests {
    
    @Autowired
    protected PlayerService playerService;
    @Autowired
	protected GameService gameService;	
    @Autowired
    protected UserService userService;

    private User user1;
    private User user2;
    private Game game1;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void createInitialPlayer() {
        user1=new User();
        user1.setUsername("UsuarioDePrueba");
        user1.setEmail("usuarioDePrueba@gmail.com");
        user1.setPassword("contraseñaDePrueba");
        userService.saveUser(user1);

        user2=new User();
        user2.setUsername("UsuarioDePrueba2");
        user2.setEmail("usuarioDePrueba2@gmail.com");
        user2.setPassword("contraseñaDePrueba2");
        userService.saveUser(user2);

        game1=new Game();
        game1.setTurn(0);
        game1.setMaxNumberOfPlayers(3);
        gameService.saveGame(game1);

        player1=new Player();
        player1.setUser(user1);
        player1.setGame(game1);
        player1.setEnergyPoints(0);
        player1.setLifePoints(10);
        player1.setVictoryPoints(0);
        player1.setLocation(LocationType.fueraTokyo);
        playerService.savePlayer(player1);
        
        player2=new Player();
        player2.setUser(user2);
        player2.setGame(game1);
        player2.setEnergyPoints(0);
        player2.setLifePoints(10);
        player2.setVictoryPoints(0);
        player2.setLocation(LocationType.ciudadTokyo);
        playerService.savePlayer(player2);


    }

    @Test
    public void testPlayerFindById(){
        Integer playerId=player1.getId();
        Player playerTest=playerService.findPlayerById(playerId);
        assertEquals(playerId, playerTest.getId());
    }

    @Test
    public void testFindPlayerWithCorrectId(){
        Player player4 = playerService.findPlayerById(4);
        assertThat(player4.getMonsterName().toString()).startsWith("CyberBunny");
		assertThat(player4.getLifePoints()).isEqualTo(0);
        assertThat(player4.getVictoryPoints()).isEqualTo(8);
        assertThat(player4.getEnergyPoints()).isEqualTo(2);
        assertThat(player4.getLocation().toString()).startsWith("bahiaTokyo");
        assertThat(player4.getGame().getId()).isEqualTo(1);
    }


    @Test 
    public void testAddPlayer(){
        int countInitial=playerService.playerCount();
        Game game2=new Game();
        game2.setTurn(0);
        game2.setMaxNumberOfPlayers(3);
        gameService.saveGame(game2);
        

        Player playerTest=new Player();
        playerTest.setUser(user1);
        playerTest.setEnergyPoints(0);
        playerTest.setLifePoints(10);
        playerTest.setVictoryPoints(0);
        playerTest.setLocation(LocationType.fueraTokyo);
        playerTest.setGame(game2);
        
        playerService.savePlayer(playerTest);
        int countAdd=playerService.playerCount();
        assertEquals(countInitial + 1, countAdd);
    }

    @Test 
    public void testHealRoll(){
        Roll roll=new Roll();
        List<DiceValues> valoresCuracion=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            valoresCuracion.add(DiceValues.HEAL);
        }
        player1.setLifePoints(1);
        roll.setValues(valoresCuracion);
        playerService.savePlayer(player1);
        try {
            playerService.useRoll(game1.getId(), player1.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player1.getLifePoints(),7);
    }

    @Test 
    public void testHealRollInTokyo() {
        Roll roll=new Roll();
        List<DiceValues> valoresCuracion=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            valoresCuracion.add(DiceValues.HEAL);
        }
        player1.setLocation(LocationType.ciudadTokyo);
        player1.setLifePoints(1);
        roll.setValues(valoresCuracion);
        playerService.savePlayer(player1);
        try {
            playerService.useRoll(game1.getId(), player1.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player1.getLifePoints(),1);
    }

    @Test 
    public void testDamageToTokyoRoll(){
        Roll roll=new Roll();
        List<DiceValues> damageValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            damageValues.add(DiceValues.ATTACK);
        }
        roll.setValues(damageValues);
        
        try {
            playerService.useRoll(game1.getId(), player1.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player2.getLifePoints(),4);
    }

    @Test 
    public void testDamageFromTokyoRoll(){
        Roll roll=new Roll();
        List<DiceValues> damageValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            damageValues.add(DiceValues.ATTACK);
        }
        roll.setValues(damageValues);
        
        try {
            playerService.useRoll(game1.getId(), player2.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player1.getLifePoints(),4);
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

    @Test 
    public void testOnesRoll(){
        Roll roll=new Roll();
        List<DiceValues> onesValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            onesValues.add(DiceValues.ONE);
        }
        
        roll.setValues(onesValues);
        
        try {
            playerService.useRoll(game1.getId(), player1.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player1.getVictoryPoints(),4);
    }

    @Test 
    public void testTwosRoll(){
        Roll roll=new Roll();
        List<DiceValues> twosValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            twosValues.add(DiceValues.TWO);
        }
        
        roll.setValues(twosValues);
        
        try {
            playerService.useRoll(game1.getId(), player1.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player1.getVictoryPoints(),5);
    }

    @Test 
    public void testThreesRoll(){
        Roll roll=new Roll();
        List<DiceValues> threesValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            threesValues.add(DiceValues.THREE);
        }
        
        roll.setValues(threesValues);
        
        try {
            playerService.useRoll(game1.getId(), player1.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player1.getVictoryPoints(),6);
    }

    @Test 
    public void testEnergyRoll(){
        Roll roll=new Roll();
        List<DiceValues> energyValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            energyValues.add(DiceValues.ENERGY);
        }
        
        roll.setValues(energyValues);
        
        try {
            playerService.useRoll(game1.getId(), player1.getId(), roll);
        } catch (DuplicatedMonsterNameException e) {
            
            e.printStackTrace();
        }
        assertEquals(player1.getEnergyPoints(),6);
    }


    

/*
    @Test
	@Transactional
	public void shouldInsertPlayerIntoDatabaseAndGenerateId() {
        Game game1 = this.gameService.findGameById(1);
        User user1 = this.userService.findUserById(1).get();
		Player player = new Player();
		player.setMonsterName(MonsterName.GigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.fueraTokyo);
        player.setGame(game1);
        player.setUser(user1);
            try {
                this.playerService.savePlayer(player);;
            } catch (DuplicatedMonsterNameException ex) {
                Logger.getLogger(PlayerServiceTests.class.getName()).log(Level.SEVERE, null, ex);
            }
		// checks that id has been generated
		assertThat(player.getId()).isNotNull();
	}
    */

/* ESTA COMENTADO POR EL TEMA DE THROW EXCEPTION 
    /*@Test
	@Transactional
	public void shouldThrowExceptionInsertingPlayersWithTheSameMonsterName(){
        Game game1 = this.gameService.findGameById(1);
        User user1 = this.userService.findUserById(1).get();
		Player player = new Player();
		player.setMonsterName(MonsterName.GigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.fueraTokyo);
        player.setGame(game1);
        player.setUser(user1);
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
  
    /*@Test
	@Transactional
	public void shouldThrowExceptionUpdatingPlayerWithTheSameMonsterName() {
        Game game1 = this.gameService.findGameById(1);
        User user1 = this.userService.findUserById(1).get();
		Player player = new Player();
		player.setMonsterName(MonsterName.GigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.fueraTokyo);
        player.setGame(game1);
        player.setUser(user1);

        Player anotherPlayer = new Player();		
		anotherPlayer.setMonsterName(MonsterName.Alien);
		anotherPlayer.setGame(game1);
        anotherPlayer.setUser(user1);
        anotherPlayer.setLifePoints(10);
        anotherPlayer.setVictoryPoints(0);
        anotherPlayer.setEnergyPoints(0);
        anotherPlayer.setLocation(LocationType.fueraTokyo);

        try {
            this.playerService.savePlayer(player);
            this.playerService.savePlayer(anotherPlayer);
        } catch (DuplicatedMonsterNameException ex) {
            ex.printStackTrace();
        }
        
		Assertions.assertThrows(DuplicatedMonsterNameException.class, () ->{
			anotherPlayer.setMonsterName(MonsterName.GigaZaur);;
			playerService.savePlayer(anotherPlayer);
		});		
	}
*/
/*
    @Test
	void shouldFindPlayerStatusByPlayerId() throws Exception {
		List<PlayerStatus> lsStatus = this.playerService.findPlayerStatus(1);
		assertThat(lsStatus.size()).isEqualTo(2);
		PlayerStatus[] pStatusArray = lsStatus.toArray(new PlayerStatus[lsStatus.size()]);
		assertThat(pStatusArray[0].getAmount()).isNotNull();
		assertThat(pStatusArray[0].getStatus()).isNotNull();
		assertThat(pStatusArray[0].getAmount()).isEqualTo(1);
		assertThat(pStatusArray[0].getStatus()).isEqualTo(StatusType.Reductor);
	}*/
    
}
