package org.springframework.samples.kingoftokyo.game;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.kingoftokyo.dice.DiceValues;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.player.LocationType;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

/**
 *@author Jose Maria Delgado Sanchez
 *@author Sara Cruz Duárez
 *@author Noelia López Durán
 */

public class GameServiceTests {
    
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired 
    private UserService userService;

    private Integer numberOfGames = 4;
    private Integer gameFinished = 1;



    @Test
    public void testSaveCardIntoDatabaseAndGenerateId() throws DataAccessException, NotFoundException{
        Game game = new Game();
        game.setTurn(0);
        game.setMaxNumberOfPlayers(6);
        gameService.saveGame(game);

        assertThat(game.getId()).isNotNull();

        Game gamedb = gameService.findGameById(game.getId());
        assertThat(game).isEqualTo(gamedb);
    }

    @Test
    public void testOnGoingGamesList(){
        List<Game> games = gameService.findOnGoingGames();
        Boolean notOnGoingGames = games.stream()
                                    .filter(g -> !g.isOnGoing())
                                    .findAny()
                                    .isPresent();
        assertThat(notOnGoingGames).isFalse();
    }


    @Test
    public void testIsRecentlyHurtToFalse() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(1);
        List<Player> lsplayer = game.getPlayers();
        gameService.isRecentlyHurtToFalse(game);
        Boolean res = false;
        for(Player player : lsplayer){
            if(player.getRecentlyHurt().equals(true))
                res = true;
        }
        assertThat(res).isFalse();
    }


    @Disabled
    @Test
    public void testChangePosition(){

        //actualizar
        Player playerActualTurn = playerService.findPlayerById(gameService.actualTurnPlayerId(1));
        LocationType firstPlayerLocation = playerActualTurn.getLocation();
        gameService.changePosition(1);
        LocationType secondPlayerLocation = playerActualTurn.getLocation();
        assertThat(secondPlayerLocation.toString()).isNotEqualTo(firstPlayerLocation.toString());
        
    }

    @Test
    public void testShouldSaveGame(){
        List<Game> listaInicial = new ArrayList<>();
        gameService.findAll().forEach(listaInicial::add);
        Integer numinicial = listaInicial.size();//conteo manual
        assertEquals(numinicial, numberOfGames);
        Integer conteoInicial = gameService.gameCount();//para comprobar gameCount()
        assertEquals(conteoInicial, numberOfGames);

        User creator = (User) userService.findUserById(5).get();//Un usuario cualquiera para crear una partida
        Game game = new Game();
        game.setCreator(creator);
        game.setName("Partida insertada");
        game.setTurn(1);
        game.setStartTime(LocalDateTime.now());
        game.setMaxNumberOfPlayers(3);
        gameService.saveGame(game);

        List<Game> listaFinal = new ArrayList<>();
        gameService.findAll().forEach(listaFinal::add);
        Integer numfinal = listaFinal.size();
        Integer numberOfGamesfinal = numberOfGames+1;
        Integer conteoFinal = gameService.gameCount();
        assertEquals(conteoFinal, numberOfGamesfinal);
        assertEquals(numfinal, numberOfGamesfinal);
    }
    
    @Test
    public void testOnePlayerShouldDie() throws DataAccessException, NotFoundException{
        Integer numberPlayers = 5; 
        Game game = gameService.findGameById(4);
        Integer playerIdActualTurn = gameService.actualTurnPlayerId(4);
        Integer numPlayersTest = game.getPlayers().size();
        assertEquals(numPlayersTest, numberPlayers);
        //Vamos a crear una tirada a mano para matar al player 1 
        Roll roll = new Roll();
        DiceValues[] guardados = new DiceValues[]{DiceValues.ATTACK,
            DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK};
        roll.setKeep(guardados);
        roll.setRollAmount(2);
        playerService.useRoll(playerIdActualTurn, roll);
        numberPlayers = numberPlayers-1;
        Integer numPlayersTest2 = game.playersAlive().size();
        assertEquals(numPlayersTest2, numberPlayers);
    }

}
