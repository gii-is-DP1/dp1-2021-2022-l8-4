package org.springframework.samples.kingoftokyo.card;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.samples.kingoftokyo.player.LocationType;
import org.springframework.samples.kingoftokyo.player.Monster;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.playercard.PlayerCard;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;


/**
 * @author Ricardo Nadal Garcia
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(SecurityConfiguration.class)

public class CardTest {
    
    @Autowired
    protected PlayerService playerService;
    @Autowired
	protected GameService gameService;	
    @Autowired
    protected UserService userService;
    @Autowired
    protected MapGameRepository mapGameRepository;

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
    void apartmentBuildingCardTest() throws DataAccessException, NotFoundException{
        Integer initialScore=player1.getVictoryPoints();
        CardEnum.APARTMENTBUILDING.effect(player1, playerService, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();
        assertEquals(initialScore+3, postScore);
    }

    @Test
    void commuterTrainCardTest() throws DataAccessException, NotFoundException{
        Integer initialScore=player1.getVictoryPoints();
        CardEnum.COMMUTERTRAIN.effect(player1, playerService, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();
        assertEquals(initialScore+2, postScore);
    }

    @Test
    void energizeCardTest() throws DataAccessException, NotFoundException{
        Integer initialEnergy=player1.getEnergyPoints();
        CardEnum.ENERGIZE.effect(player1, playerService, mapGameRepository);
        Integer postEnergy=playerService.findPlayerById(player1.getId()).getEnergyPoints();
        assertEquals(initialEnergy+9, postEnergy);
    }

    @Test
    void fireblastCardTest() throws DataAccessException, NotFoundException{
        Integer initialLife=player1.getLifePoints();
        CardEnum.FIREBLAST.effect(player2, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player1.getId()).getLifePoints();
        assertEquals(initialLife-2, postLife);
    }

    @Test
    void evacuationOrdersCardTest() throws DataAccessException, NotFoundException{
        player2.setVictoryPoints(player2.getVictoryPoints()+5);
        Integer initialScore=player2.getVictoryPoints();
        CardEnum.EVACUATIONORDERS.effect(player1, playerService, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player2.getId()).getVictoryPoints();
        assertEquals(initialScore-5, postScore);
    }

    @Test
    void healCardTest() throws DataAccessException, NotFoundException{
        player1.setLifePoints(8);
        Integer initialLife=player1.getLifePoints();
        CardEnum.HEAL.effect(player1, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player1.getId()).getLifePoints();
        assertEquals(initialLife+2, postLife);
    }

    @Test
    void refineryGasCardTest() throws DataAccessException, NotFoundException{
        Integer initialLife=player1.getLifePoints();
        Integer initialScore=player2.getVictoryPoints();
        CardEnum.GASREFINERY.effect(player2, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player1.getId()).getLifePoints();
        Integer postScore=playerService.findPlayerById(player2.getId()).getVictoryPoints();
        assertEquals(initialLife-3, postLife);
        assertEquals(initialScore+2, postScore);
    }

    @Test
    void highAltitudeBombingCardTest() throws DataAccessException, NotFoundException{
        Integer initialLife1=player1.getLifePoints();
        Integer initialLife2=player2.getLifePoints();
        CardEnum.HIGHALTITUDEBOMBING.effect(player2, playerService, mapGameRepository);
        Integer postLife1=playerService.findPlayerById(player1.getId()).getLifePoints();
        Integer postLife2=playerService.findPlayerById(player2.getId()).getLifePoints();
        assertEquals(initialLife1-3, postLife1);
        assertEquals(initialLife2-3, postLife2);
    }

    @Test
    void jetFighterCardTest() throws DataAccessException, NotFoundException{
        Integer initialLife=player2.getLifePoints();
        Integer initialScore=player2.getVictoryPoints();
        CardEnum.JETFIGHTERS.effect(player2, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player2.getId()).getLifePoints();
        Integer postScore=playerService.findPlayerById(player2.getId()).getVictoryPoints();
        assertEquals(initialLife-4, postLife);
        assertEquals(initialScore+5, postScore);
    }

    @Test
    void nationalGuardCardTest() throws DataAccessException, NotFoundException{
        Integer initialLife=player2.getLifePoints();
        Integer initialScore=player2.getVictoryPoints();
        CardEnum.NATIONALGUARD.effect(player2, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player2.getId()).getLifePoints();
        Integer postScore=playerService.findPlayerById(player2.getId()).getVictoryPoints();
        assertEquals(initialLife-2, postLife);
        assertEquals(initialScore+2, postScore);
    }

    @Test
    void cornerStoreCardTest() throws DataAccessException, NotFoundException{
        Integer initialScore=player1.getVictoryPoints();
        CardEnum.CORNERSTORE.effect(player1, playerService, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();
        assertEquals(initialScore+1, postScore);
    }

    @Test
    void evenBiggerCardTest() throws DataAccessException, NotFoundException{
        player1.setLifePoints(8);
        Integer initialLife=player1.getLifePoints();
        CardEnum.EVENBIGGER.effect(player1, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player1.getId()).getLifePoints();
        assertEquals(initialLife+2, postLife);
    }

    @Test
    void accidAttackCardTest() {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        Integer initialSize=initialRoll.getCardExtraValues().size();
        CardEnum.ACIDATTACK.effectInRoll(player1, playerService, mapGameRepository);
        Roll postRoll=mapGameRepository.getRoll(game1.getId());
        assertEquals(initialSize+1, postRoll.getCardExtraValues().size());
    }

    @Test
    void alphaMonsterCardTest() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ATTACK);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        
        Integer initialScore=player1.getVictoryPoints();

        CardEnum.ALPHAMONSTER.effectInRoll(player1, playerService, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();
        
        assertEquals(initialScore +1, postScore);
    }

    @Test
    void fireBreathingCardTestInTokyo() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ATTACK);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        player2.setLocation(LocationType.FUERATOKYO);
        List<Player> players=new ArrayList<>();
        players.add(player1);
        players.add(player2);
        game1.setPlayers(players);
        player1.setGame(game1);

        Integer initialLife=player2.getLifePoints();

        CardEnum.FIREBREATHING.effectInRoll(player1, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player2.getId()).getLifePoints();
        
        assertEquals(initialLife -1 , postLife);
    }

    @Test
    void fireBreathingCardTestOutOfTokyo() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ATTACK);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        player1.setLocation(LocationType.BAHIATOKYO);
        List<Player> players=new ArrayList<>();
        players.add(player1);
        players.add(player2);
        game1.setPlayers(players);
        player1.setGame(game1);

        Integer initialLife=player2.getLifePoints();

        CardEnum.FIREBREATHING.effectInRoll(player1, playerService, mapGameRepository);
        Integer postLife=playerService.findPlayerById(player2.getId()).getLifePoints();
        
        assertEquals(initialLife -1 , postLife);
    }

    @Test
    void friendOfChildrenCardTest() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ENERGY);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        
        Integer initialRollSize=initialRoll.getCardExtraValues().size();

        CardEnum.FRIENDOFCHILDREN.effectInRoll(player1, playerService, mapGameRepository);
        Integer postRollSize=mapGameRepository.getRoll(game1.getId()).getCardExtraValues().size();
        
        assertEquals(initialRollSize+1, postRollSize);
    }

    @Test
    void extraHeadCardTest() {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        Integer initialSize=initialRoll.getValues().size();
        CardEnum.EXTRAHEAD.effectStartTurn(player1, playerService, mapGameRepository);
        Roll postRoll=mapGameRepository.getRoll(game1.getId());
        assertEquals(initialSize + 1, postRoll.getValues().size());
    }

    @Test
    void giantBrainCardTest() {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        Integer initialMaxThrows=initialRoll.getMaxThrows();
        CardEnum.GIANTBRAIN.effectStartTurn(player1, playerService, mapGameRepository);
        Integer postMaxThrows=mapGameRepository.getRoll(game1.getId()).getMaxThrows();
        assertEquals(initialMaxThrows + 1, postMaxThrows);
    }

    @Test
    void completeDestructionCardTest() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ENERGY);
        rollValues.add(DiceValues.ATTACK);
        rollValues.add(DiceValues.HEAL);
        rollValues.add(DiceValues.ONE);
        rollValues.add(DiceValues.TWO);
        rollValues.add(DiceValues.THREE);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        
        Integer initialScore=player1.getVictoryPoints();

        CardEnum.COMPLETEDESTRUCTION.effectInRoll(player1, playerService, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();
        
        assertEquals(initialScore+9, postScore);
    }

    @Test
    void armorPlatingTest() {
        Integer initialDamage=1;
        Integer postDamage=CardEnum.ARMORPLATING.effectDamage(player1, playerService, initialDamage, mapGameRepository);
        assertEquals(initialDamage-1, postDamage);
    }

    @Test
    void camouflageTest() {
        Integer initialDamage=6;
        Integer postDamage=CardEnum.CAMOUFLAGE.effectDamage(player1, playerService, initialDamage, mapGameRepository);
        Boolean inRange= postDamage >=0 && postDamage<=initialDamage;
        assertEquals(true, inRange);
    }

    @Test
    void itHasAChildTest() {
        Integer initialDamage=99;
        Integer postDamage=CardEnum.ITHASACHILD.effectDamage(player1, playerService, initialDamage, mapGameRepository);
        assertEquals(0, postDamage);
        assertEquals(player1.getMaxHealth(), player1.getLifePoints());
    }

    @Test
    void alienMetabolismTest() {
        Integer initialEnergy=1;
        Integer cost=0;
        Integer postEnergy=CardEnum.ALIENMETABOLISM.effectBuy(player1, playerService, initialEnergy, cost, mapGameRepository);
        assertEquals(initialEnergy+1, postEnergy);
    }

    @Test
    void dedicatedNewsTeamTest() throws DataAccessException, NotFoundException {
        Integer initialEnergy=1;
        Integer cost=0;
        Integer initialScore=player1.getVictoryPoints();
        CardEnum.DEDICATEDNEWSTEAM.effectBuy(player1, playerService, initialEnergy, cost, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();
        assertEquals(initialScore+1, postScore);
    }

    @Test
    void herbivoreTestWithoutAttack() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());

        Integer initialScore=player1.getVictoryPoints();

        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ENERGY);
        rollValues.add(DiceValues.HEAL);
        rollValues.add(DiceValues.ONE);
        rollValues.add(DiceValues.TWO);
        rollValues.add(DiceValues.THREE);        
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);

        CardEnum.HERBIVORE.effectEndTurn(player1, playerService, mapGameRepository);

        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();

        assertEquals(initialScore+1, postScore);
    }

    @Test
    void herbivoreTestWithAttack() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());

        Integer initialScore=player1.getVictoryPoints();

        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ENERGY);
        rollValues.add(DiceValues.HEAL);
        rollValues.add(DiceValues.ONE);
        rollValues.add(DiceValues.ATTACK);
        rollValues.add(DiceValues.TWO);
        rollValues.add(DiceValues.THREE);        
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);

        CardEnum.HERBIVORE.effectEndTurn(player1, playerService, mapGameRepository);

        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();

        assertEquals(initialScore, postScore);
    }

    @Test
    void energyHoarderTest() throws DataAccessException, NotFoundException {
        Integer initialEnergy=6;
        player1.setEnergyPoints(initialEnergy);
        Integer initialScore=player1.getVictoryPoints();
        CardEnum.ENERGYHOARDER.effectEndTurn(player1, playerService, mapGameRepository);

        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();

        assertEquals(initialScore+1,postScore);
    }

    @Test
    void novaBreathTestInTokyo() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ATTACK);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        player1.setLocation(LocationType.BAHIATOKYO);
        List<Player> players=new ArrayList<>();
        players.add(player1);
        players.add(player2);
        game1.setPlayers(players);
        player1.setGame(game1);

        Integer initialLife=player2.getLifePoints();

        CardEnum.NOVABREATH.effectAfterRoll(player1, playerService, mapGameRepository);;
        Integer postLife=playerService.findPlayerById(player2.getId()).getLifePoints();
        
        assertEquals(initialLife -1 , postLife);
    }

    @Test
    void novaBreathTestOutOfTokyo() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ATTACK);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        player2.setLocation(LocationType.FUERATOKYO);
        List<Player> players=new ArrayList<>();
        players.add(player1);
        players.add(player2);
        game1.setPlayers(players);
        player1.setGame(game1);

        Integer initialLife=player2.getLifePoints();

        CardEnum.NOVABREATH.effectAfterRoll(player1, playerService, mapGameRepository);;
        Integer postLife=playerService.findPlayerById(player2.getId()).getLifePoints();
        
        assertEquals(initialLife -1 , postLife);
    }

    @Test
    void gourmetCardTest() throws DataAccessException, NotFoundException {
        Roll initialRoll=mapGameRepository.getRoll(game1.getId());
        List<DiceValues> rollValues=new ArrayList<>();
        rollValues.add(DiceValues.ONE);
        rollValues.add(DiceValues.ONE);
        rollValues.add(DiceValues.ONE);
        initialRoll.setValues(rollValues);
        mapGameRepository.putRoll(game1.getId(), initialRoll);
        
        Integer initialScore=player1.getVictoryPoints();

        CardEnum.GOURMET.effectInRoll(player1, playerService, mapGameRepository);
        Integer postScore=playerService.findPlayerById(player1.getId()).getVictoryPoints();
        
        assertEquals(initialScore+2, postScore);
    }



    

}
