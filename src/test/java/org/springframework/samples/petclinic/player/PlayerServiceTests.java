package org.springframework.samples.petclinic.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.LocationType;
import org.springframework.samples.petclinic.player.Monster;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.samples.petclinic.playercard.PlayerCard;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 *@author Noelia López Durán
 *@author Ricardo Nadal Garcia
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))


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
        game1.setName("Partida prueba");
        game1.setCreator(user1);
        game1.setTurn(0);
        game1.setPlayers(new ArrayList<Player>());
        game1.setMaxNumberOfPlayers(3);
        gameService.saveGame(game1);

        Game gameBefore = gameService.findGameById(game1.getId());
        player1=new Player();
        player1.setPlayerCard(new ArrayList<PlayerCard>());
        player1.setMonster(Monster.alien);
        player1.setUser(user1);
        player1.setGame(gameBefore);
        player1.setEnergyPoints(0);
        player1.setLifePoints(10);
        player1.setVictoryPoints(0);
        player1.setLocation(LocationType.fueraTokyo);
        player1.setRecentlyHurt(Boolean.FALSE);
        playerService.savePlayer(player1);
        
        player2=new Player();
        player2.setPlayerCard(new ArrayList<PlayerCard>());
        player2.setMonster(Monster.cyberBunny);
        player2.setUser(user2);
        player2.setGame(gameBefore);
        player2.setEnergyPoints(0);
        player2.setLifePoints(10);
        player2.setVictoryPoints(0);
        player2.setLocation(LocationType.ciudadTokyo);
        player2.setRecentlyHurt(Boolean.FALSE);
        playerService.savePlayer(player2);

        //Esto lo hago ya que la lista de players no se guarda al hacer saveplayer como deberia (solo ocurre en el test)
       
        gameBefore.getPlayers().add(player1);
        gameBefore.getPlayers().add(player2);
        gameService.saveGame(gameBefore);
        


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
        assertThat(player4.getMonster().getName()).startsWith("CyberBunny");
		assertThat(player4.getLifePoints()).isEqualTo(0);
        assertThat(player4.getVictoryPoints()).isEqualTo(8);
        assertThat(player4.getEnergyPoints()).isEqualTo(2);
        assertThat(player4.getLocation().toString()).startsWith("bahiaTokyo");
        assertThat(player4.getGame().getId()).isEqualTo(1);
    }

    @Test
    public void testCountPlayers() {
        Integer size=(int) StreamSupport.stream(playerService.findAll().spliterator(),false).count();
        assertEquals(size,playerService.playerCount());
    }


    @Test 
    public void testAddPlayer(){
        int countInitial=playerService.playerCount();
        Game game2=new Game();
        game2.setName("Partida prueb2");
        game2.setTurn(0);
        game2.setMaxNumberOfPlayers(3);
        gameService.saveGame(game2);
        

        Player playerTest=new Player();
        playerTest.setUser(user1);
        playerTest.setMonster(Monster.cyberBunny);
        playerTest.setEnergyPoints(0);
        playerTest.setLifePoints(10);
        playerTest.setVictoryPoints(0);
        playerTest.setLocation(LocationType.fueraTokyo);
        playerTest.setRecentlyHurt(Boolean.FALSE);
        playerTest.setGame(game2);
        
        playerService.savePlayer(playerTest);
        int countAdd=playerService.playerCount();
        assertEquals(countInitial + 1, countAdd);
    }

    
    @Test
    public void testJoinGame(){
        User testUser=new User();
        testUser.setUsername("UsuarioDePruebaJoin");
        testUser.setEmail("usuarioDePruebaJoin@gmail.com");
        testUser.setPassword("contraseñaDePruebaJoin");
        testUser.setGames(new HashSet<Game>());
        userService.saveUser(testUser);

        Game gameTest=new Game();
        List<Player> playersList=new ArrayList<Player>();
        gameTest.setTurn(0);
        gameTest.setPlayers(playersList);
    
        gameTest.setMaxNumberOfPlayers(3);
        gameService.saveGame(gameTest);

        Player newPlayer=new Player();
        newPlayer.setUser(testUser);
        newPlayer.setMonster(Monster.king); 

        playerService.joinGame(newPlayer, gameTest);
        assertEquals(1, gameTest.getPlayers().size());
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
        
        playerService.useRoll(player1.getId(), roll);
        
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
        
        playerService.useRoll(player1.getId(), roll);
        
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
        
        playerService.useRoll(player1.getId(), roll);
        
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
        
        playerService.useRoll(player2.getId(), roll);
        
        assertEquals(player1.getLifePoints(),4);
    }

    @Test
	@Transactional
	public void shouldUpdateMonsterName() {
		Player player2 = this.playerService.findPlayerById(2);

		Monster newName = Monster.alien;
		player2.setMonster(newName);
		this.playerService.savePlayer(player2);

		player2 = this.playerService.findPlayerById(2);
		assertThat(player2.getMonster()).isEqualTo(newName);
	}

    @Test 
    public void testOnesRoll(){
        Roll roll=new Roll();
        List<DiceValues> onesValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            onesValues.add(DiceValues.ONE);
        }
        
        roll.setValues(onesValues);
        
        playerService.useRoll(player1.getId(), roll);
        
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
        
        playerService.useRoll(player1.getId(), roll);

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
        
        playerService.useRoll(player1.getId(), roll);

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
        
        playerService.useRoll(player1.getId(), roll);

        assertEquals(player1.getEnergyPoints(),6);
    }

    @Test
    public void startTurnInTokyoTest() {
        assertEquals(player2.getVictoryPoints(), 0);
        playerService.startTurn(player2.getId());
        assertEquals(player2.getVictoryPoints(), 2);
    }

    @Test
    public void startTurnOutOfTokyoTest() {
        assertEquals(player1.getVictoryPoints(), 0);
        playerService.startTurn(player1.getId());
        assertEquals(player1.getVictoryPoints(), 0);
    }

    @Test 
    public void testEnterTokyoRoll(){
        Roll roll=new Roll();
        player2.setLocation(LocationType.fueraTokyo);
        List<DiceValues> damageValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            damageValues.add(DiceValues.ATTACK);
        }
        roll.setValues(damageValues);
        
        playerService.useRoll(player1.getId(), roll);
        
        assertEquals(player1.getLocation(),LocationType.ciudadTokyo);
    } 

    @Test   
	@Transactional
	public void shouldInsertPlayerIntoDatabaseAndGenerateId() {
        
        User user1 = this.userService.findUserById(1).get();
		Player player = new Player();
		player.setMonster(Monster.gigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.fueraTokyo);
        player.setRecentlyHurt(Boolean.FALSE);
        player.setGame(game1);
        player.setUser(user1);
            
        this.playerService.savePlayer(player);;
            
		// checks that id has been generated
		assertThat(player.getId()).isNotNull();
	}
	
  
    @Test
    @Disabled
	@Transactional
	public void shouldThrowExceptionUpdatingPlayerWithTheSameMonsterName() { //Actualmente no comprueba esto en la base de datos, solo lo hace al hacer JoinGame
        User user1 = this.userService.findUserById(1).get();
		Player player = new Player();
		player.setMonster(Monster.gigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.fueraTokyo);
        player.setGame(game1);
        player.setUser(user1);

        Player anotherPlayer = new Player();		
		anotherPlayer.setMonster(Monster.alien);
		anotherPlayer.setGame(game1);
        anotherPlayer.setUser(user1);
        anotherPlayer.setLifePoints(10);
        anotherPlayer.setVictoryPoints(0);
        anotherPlayer.setEnergyPoints(0);
        anotherPlayer.setLocation(LocationType.fueraTokyo);

        
        this.playerService.savePlayer(player);
        this.playerService.savePlayer(anotherPlayer);
        
		}		
	}


