package org.springframework.samples.petclinic.game;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.springframework.samples.petclinic.player.LocationType;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

/**
 *@author Jose Maria Delgado Sanchez
 *@author Sara Cruz Duárez
 *@author Noelia López Durán
 */

public class GameServiceTests {
    
    private GameService gameService;
    private PlayerService playerService;
    private UserService userService;

    @Autowired
    public GameServiceTests(GameService gameService,PlayerService playerService,UserService userService){
        this.gameService=gameService;
        this.playerService = playerService;
        this.userService = userService;
    }

    private Integer numberOfGames = 5;
    private Integer gameFinished = 1;



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

        //actualizar
        Player playerActualTurn = playerService.findPlayerById(gameService.actualTurnPlayerId(1));
        LocationType firstPlayerLocation = playerActualTurn.getLocation();
        gameService.changePosition(1);
        LocationType secondPlayerLocation = playerActualTurn.getLocation();
        assertThat(secondPlayerLocation.toString()).isNotEqualTo(firstPlayerLocation.toString());
        
    }
    @Test
    public void testShouldSaveGame(){
        Game game = gameService.findGameById(2);//Un game acabado
        String winner = game.getWinner();
        String newWinner = "user3";
        game.setWinner(newWinner);//asignamos nuevo ganador
        gameService.saveGame(game);
        assertNotEquals(game.getWinner(), winner);
        assertEquals(game.getWinner(),newWinner,"The winner should be 'user3'");
    }

    @Test
    public void testShouldCreateGame(){
        List<Game> listaInicial = new ArrayList<>();
        gameService.findAll().forEach(listaInicial::add);
        Integer numinicial = listaInicial.size();//conteo manual usando findAll()
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
    public void testEndGameWithVictoryPoints(){
        Integer gamesPlayingOriginal = numberOfGames-gameFinished; //4
        Game game = gameService.findGameById(4);
        Player player = gameService.actualTurn(4);
        Integer gamesPlayingCalculated = gameService.findAllNotFinished().size();
        Integer finishedGamesCalculated = gameService.findAllFinished().size();
        assertEquals(gamesPlayingOriginal, gamesPlayingCalculated);
        assertEquals(gameFinished,finishedGamesCalculated);
        player.setVictoryPoints(20);
        playerService.savePlayer(player);
        gameService.saveGame(game);
        gameService.endGame(4);
        Integer finishedGamesCalculated2 = gameService.findAllFinished().size();
        assertEquals(gameFinished+1, finishedGamesCalculated2 );
        Integer gamesPlayingCalculated2 = gameService.findAllNotFinished().size();
        assertThat(gamesPlayingOriginal).isGreaterThan(gamesPlayingCalculated2);
    }

    @Test
    public void testEndGameKillingEveryone(){
        Integer gamesPlayingOriginal = numberOfGames-gameFinished;
        Game game = gameService.findGameById(5);
        Player player = gameService.actualTurn(5);
        player.setLifePoints(0);
        playerService.savePlayer(player);
        gameService.saveGame(game);
        gameService.endGame(5);
        Integer finishedGamesCalculated = gameService.findAllFinished().size();
        assertEquals(gameFinished+1, finishedGamesCalculated ); //Debería haber un game finished más que antes
        Integer gamesPlayingCalculated = gameService.findAllNotFinished().size();
        //Vamos a comprobar que el numero de partidas jugando ahora es 1 menos que antes
        assertThat(gamesPlayingOriginal).isGreaterThan(gamesPlayingCalculated);
        
    }
    
    @Test
    public void testOnePlayerShouldDie(){
        Integer numberPlayers = 5; 
        Game game = gameService.findGameById(4);
        Integer playerIdActualTurn = gameService.actualTurnPlayerId(4);
        Integer numPlayersTest = gameService.findPlayerList(4).size();
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
    @Disabled
    @Test
    public void testOnePlayerTurn(){
        Player playerInitiaPlayer = gameService.actualTurn(4);
        Roll rollkeep = new Roll(); //una tirada cualquiera sin tener ningun dado guardado 

        Game game = gameService.findGameById(4);
        Integer initialTurn = game.getTurn();
        gameService.handleTurnAction(4, Boolean.TRUE, rollkeep);
        Player playerActualTurn = gameService.actualTurn(4);
        gameService.saveGame(game);
        assertNotEquals(game.getTurn(), initialTurn);
        assertNotEquals(playerActualTurn.getMonster(), playerInitiaPlayer.getMonster());
    }

}
