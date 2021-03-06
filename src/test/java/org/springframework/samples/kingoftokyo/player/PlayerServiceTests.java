package org.springframework.samples.kingoftokyo.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.dice.DiceValues;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.game.GameService;
import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.samples.kingoftokyo.game.exceptions.NewGameException;
import org.springframework.samples.kingoftokyo.playercard.PlayerCard;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

/** 
 *@author Noelia López Durán
 *@author Ricardo Nadal Garcia
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({SecurityConfiguration.class, MapGameRepository.class})

class PlayerServiceTests {
    
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
    void createInitialPlayer() throws DataAccessException, NotFoundException {
        user1=new User();
        user1.setUsername("UsuarioDePrueba");
        user1.setEmail("usuarioDePrueba@gmail.com");
        user1.setPassword("contraseñaDePrueba");
        user1.setMaxTurnsTokyo(0l);
        userService.saveUser(user1,true);

        user2=new User();
        user2.setUsername("UsuarioDePrueba2");
        user2.setEmail("usuarioDePrueba2@gmail.com");
        user2.setPassword("contraseñaDePrueba2");
        user2.setMaxTurnsTokyo(0l);
        userService.saveUser(user2,true);
    
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
        player1.setTurnsTokyo(0l);
        player1.setEnergyPoints(0);
        player1.setLifePoints(10);
        player1.setVictoryPoints(0);
        player1.setLocation(LocationType.FUERATOKYO);
        player1.setRecentlyHurt(Boolean.FALSE);
        playerService.savePlayer(player1);
        
        player2=new Player();
        player2.setPlayerCard(new ArrayList<PlayerCard>());
        player2.setMonster(Monster.cyberBunny);
        player2.setUser(user2);
        player2.setGame(gameBefore);
        player2.setTurnsTokyo(0l);
        player2.setEnergyPoints(0);
        player2.setLifePoints(10);
        player2.setVictoryPoints(0);
        player2.setLocation(LocationType.CIUDADTOKYO);
        player2.setRecentlyHurt(Boolean.FALSE);
        playerService.savePlayer(player2);

        //Esto lo hago ya que la lista de players no se guarda al hacer saveplayer como deberia (solo ocurre en el test)
       
        gameBefore.getPlayers().add(player1);
        gameBefore.getPlayers().add(player2);
        gameService.saveGame(gameBefore);
        


    }

    @Test
    void testPlayerFindById() throws DataAccessException, NotFoundException{
        Integer playerId=player1.getId();
        Player playerTest=playerService.findPlayerById(playerId);
        assertEquals(playerId, playerTest.getId());
    }
  

    @Test
    void testFindPlayerWithCorrectId() throws DataAccessException, NotFoundException{
        Player player4 = playerService.findPlayerById(4);
        assertThat(player4.getMonster().getName()).startsWith("CyberBunny");
		assertThat(player4.getLifePoints()).isZero();
        assertThat(player4.getVictoryPoints()).isEqualTo(8);
        assertThat(player4.getEnergyPoints()).isEqualTo(2);
        assertThat(player4.getLocation().toString()).startsWith("BAHIATOKYO");
        assertThat(player4.getGame().getId()).isEqualTo(1);
    }

    @Test
    void testCountPlayers() {
        Integer size=(int) StreamSupport.stream(playerService.findAll().spliterator(),false).count();
        assertEquals(size,playerService.playerCount());
    }


    @Test 
    void testAddPlayer(){
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
        playerTest.setLocation(LocationType.FUERATOKYO);
        playerTest.setRecentlyHurt(Boolean.FALSE);
        playerTest.setGame(game2);
        
        playerService.savePlayer(playerTest);
        int countAdd=playerService.playerCount();
        assertEquals(countInitial + 1, countAdd);
    }

    
    @Test
    void testJoinGame() throws NewGameException{
        User testUser=new User();
        testUser.setUsername("UsuarioDePruebaJoin");
        testUser.setEmail("usuarioDePruebaJoin@gmail.com");
        testUser.setPassword("contraseñaDePruebaJoin");
        testUser.setGames(new HashSet<Game>());
        userService.saveUser(testUser,true);

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
    void testHealRoll(){
        Roll roll=new Roll();
        List<DiceValues> valoresCuracion=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            valoresCuracion.add(DiceValues.HEAL);
        }
        player1.setLifePoints(1);
        roll.setValues(valoresCuracion);
        playerService.savePlayer(player1);
        
        playerService.useRoll(player1, roll);
        
        assertEquals(7,player1.getLifePoints());
    }

    @Test 
    void testHealRollInTokyo() {
        Roll roll=new Roll();
        List<DiceValues> valoresCuracion=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            valoresCuracion.add(DiceValues.HEAL);
        }
        player1.setLocation(LocationType.CIUDADTOKYO);
        player1.setLifePoints(1);
        roll.setValues(valoresCuracion);
        playerService.savePlayer(player1);
        
        playerService.useRoll(player1, roll);
        
        assertEquals(1,player1.getLifePoints());
    }

    @Test 
    void testDamageToTokyoRoll(){
        Roll roll=new Roll();
        List<DiceValues> damageValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            damageValues.add(DiceValues.ATTACK);
        }
        roll.setValues(damageValues);
        
        playerService.useRoll(player1, roll);
        
        assertEquals(player2.getLifePoints(),4);
    }

    @Test 
    void testDamageFromTokyoRoll(){
        Roll roll=new Roll();
        List<DiceValues> damageValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            damageValues.add(DiceValues.ATTACK);
        }
        roll.setValues(damageValues);
        
        playerService.useRoll(player2, roll);
        
        assertEquals(4,player1.getLifePoints());
    }

    @Test
	void shouldUpdateMonsterName() throws DataAccessException, NotFoundException {
		Player player2 = this.playerService.findPlayerById(2);

		Monster newName = Monster.alien;
		player2.setMonster(newName);
		this.playerService.savePlayer(player2);

		player2 = this.playerService.findPlayerById(2);
		assertThat(player2.getMonster()).isEqualTo(newName);
	}

    @Test 
    void testOnesRoll(){
        Roll roll=new Roll();
        List<DiceValues> onesValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            onesValues.add(DiceValues.ONE);
        }
        
        roll.setValues(onesValues);
        
        playerService.useRoll(player1, roll);
        
        assertEquals(4,player1.getVictoryPoints());
    }

    @Test 
    void testTwosRoll(){
        Roll roll=new Roll();
        List<DiceValues> twosValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            twosValues.add(DiceValues.TWO);
        }
        
        roll.setValues(twosValues);
        
        playerService.useRoll(player1, roll);

        assertEquals(5,player1.getVictoryPoints());
    }

    @Test 
    void testThreesRoll(){
        Roll roll=new Roll();
        List<DiceValues> threesValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            threesValues.add(DiceValues.THREE);
        }
        
        roll.setValues(threesValues);
        
        playerService.useRoll(player1, roll);

        assertEquals(6,player1.getVictoryPoints());
    }

    @Test 
    void testEnergyRoll(){
        Roll roll=new Roll();
        List<DiceValues> energyValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            energyValues.add(DiceValues.ENERGY);
        }
        
        roll.setValues(energyValues);
        
        playerService.useRoll(player1, roll);

        assertEquals(6,player1.getEnergyPoints());
    }

    @Test
    void startTurnInTokyoTest() {
        assertEquals(player2.getVictoryPoints(), 0);

        
        playerService.startTurn(player2);
        assertEquals(2, player2.getVictoryPoints());
    }

    @Test
    void startTurnOutOfTokyoTest() {
        assertEquals(player1.getVictoryPoints(), 0);
 
        playerService.startTurn(player1);
        assertEquals(0, player1.getVictoryPoints());
    }

    @Test 
    void testEnterTokyoRoll(){
        Roll roll=new Roll();
        player2.setLocation(LocationType.FUERATOKYO);
        List<DiceValues> damageValues=new ArrayList<DiceValues>();
        for(int i=0;i<6;i++){
            damageValues.add(DiceValues.ATTACK);
        }
        roll.setValues(damageValues);
        
        playerService.useRoll(player1, roll);
        
        assertEquals(LocationType.CIUDADTOKYO,player1.getLocation());
    } 

    @Test   
	void shouldInsertPlayerIntoDatabaseAndGenerateId() { 
        User user1 = this.userService.findUserById(1);
		Player player = new Player();
		player.setMonster(Monster.gigaZaur);
        player.setLifePoints(10);
        player.setVictoryPoints(2);
        player.setEnergyPoints(6);
        player.setLocation(LocationType.FUERATOKYO);
        player.setRecentlyHurt(Boolean.FALSE);
        player.setGame(game1);
        player.setUser(user1);
            
        this.playerService.savePlayer(player);;
            
		// checks that id has been generated
		assertThat(player.getId()).isNotNull();
	}
}
	
  


