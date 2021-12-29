package org.springframework.samples.petclinic.game;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import javax.persistence.EnumType;

import org.springframework.samples.petclinic.player.LocationType;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.Returns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

/**
 *@author Jose Maria Delgado Sanchez
 *@author Sara Cruz Duárez
 */

public class GameServiceTests {
    
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;


    @Test
    public void testSaveCardIntoDatabaseAndGenerateId(){
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
    public void testCreateNewGamePositive(){
        User user = userService.findUserById(1).get();
        Game g = new Game();
        g.setName("testGame");
        g.setMaxNumberOfPlayers(5);
        Game game = gameService.createNewGame(user, g);
        assertThat(game.getCreator()).isEqualTo(user);
        assertThat(game.getTurn()).isZero();
    }
    // caso negativo ?

    @Test 
    public void testDeleteGameByCreator(){
        User user = userService.findUserById(1).get();
        Game game = new Game();
        game.setCreator(user);
        gameService.deleteGameByCreator(user, game);
        assertThat(gameService.findOnGoingGames().contains(game)).isFalse();
    } 
    // borrar juegos ya terminados?


    @Test
    public void testIsRecentlyHurtToFalse(){
        List<Player> lsplayer = gameService.findPlayerList(1);
        gameService.isRecentlyHurtToFalse(1);
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

        // hacer cuando esté la bd de tests
        Player playerActualTurn = playerService.findPlayerById(gameService.actualTurnPlayerId(1));
        LocationType firstPlayerLocation = playerActualTurn.getLocation();
        gameService.changePosition(1);
        LocationType secondPlayerLocation = playerActualTurn.getLocation();
        assertThat(secondPlayerLocation.toString()).isNotEqualTo(firstPlayerLocation.toString());

    }



}
