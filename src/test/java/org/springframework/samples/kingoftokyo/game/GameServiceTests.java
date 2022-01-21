package org.springframework.samples.kingoftokyo.game;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.dice.DiceValues;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.game.exceptions.DeleteGameException;
import org.springframework.samples.kingoftokyo.game.exceptions.NewGameException;
import org.springframework.samples.kingoftokyo.player.LocationType;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.player.exceptions.InvalidPlayerActionException;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;


import javassist.NotFoundException;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({SecurityConfiguration.class, MapGameRepository.class})
/**
 *@author Jose Maria Delgado Sanchez
 *@author Sara Cruz Duárez
 *@author Noelia López Durán
 */

 
class GameServiceTests {
    
    private MapGameRepository mapGameRepository;
    private GameService gameService;
    private PlayerService playerService;
    private UserService userService;

    @Autowired
    private GameServiceTests(GameService gameService,PlayerService playerService,UserService userService
    , MapGameRepository mapGameRepository){
        this.gameService=gameService;
        this.playerService = playerService;
        this.userService = userService;
        this.mapGameRepository = mapGameRepository;
        
    }

    private Integer numberOfGames = 7;
    private Integer gamesStarted = 4;
    private Integer gameFinished = 1;
    private Integer gamesPlaying = numberOfGames-gameFinished;
    private Integer gamesInLobby = gamesPlaying-gamesStarted;



    @Test
    void testSaveCardIntoDatabaseAndGenerateId() throws DataAccessException, NotFoundException{
        Game game = new Game();
        game.setTurn(0);
        game.setMaxNumberOfPlayers(6);
        gameService.saveGame(game);

        assertThat(game.getId()).isNotNull();

        Game gamedb = gameService.findGameById(game.getId());
        assertThat(game).isEqualTo(gamedb);
    }

    @Test
    void testOnGoingGamesList(){
        List<Game> games = gameService.findOnGoingGames();
        Boolean notOnGoingGames = games.stream()
                                    .filter(g -> !g.isOnGoing())
                                    .findAny()
                                    .isPresent();
        assertThat(notOnGoingGames).isFalse();
    }


    @Test
    void testIsRecentlyHurtToFalse() throws DataAccessException, NotFoundException{
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
    
    @Test
    void testShouldSaveGame() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(2);//Un game acabado
        String winner = game.getWinner();
        String newWinner = "user3";
        game.setWinner(newWinner);//asignamos nuevo ganador
        gameService.saveGame(game);
        assertNotEquals(game.getWinner(), winner);
        assertEquals(game.getWinner(),newWinner,"The winner should be 'user3'");
    }

    @Test
    void testShouldCreateGame() throws NewGameException{
        List<Game> listaInicial = new ArrayList<>();
        gameService.findAll().forEach(listaInicial::add);
        Integer numinicial = listaInicial.size();//conteo manual usando findAll()
        assertEquals(numinicial, numberOfGames);
        Integer conteoInicial = gameService.gameCount();//para comprobar gameCount()
        assertEquals(conteoInicial, numberOfGames);

        User creator = (User) userService.findUserById(5);//Un usuario cualquiera para crear una partida
        Game game = new Game();
        game.setCreator(creator);
        game.setName("Partida insertada");
        game.setTurn(1);
        game.setStartTime(LocalDateTime.now());
        game.setMaxNumberOfPlayers(3);
        gameService.createNewGame(game);

        List<Game> listaFinal = new ArrayList<>();
        gameService.findAll().forEach(listaFinal::add);
        Integer numfinal = listaFinal.size();
        Integer numberOfGamesfinal = numberOfGames+1;
        Integer conteoFinal = gameService.gameCount();
        assertEquals(conteoFinal, numberOfGamesfinal);
        assertEquals(numfinal, numberOfGamesfinal);
    }

    @Test
    void testShouldNotCreateGameBecauseCreatorAlreadyInGame() throws NewGameException{
        User creator = (User) userService.findUserById(3);//Un usuario en partida
        Game game = new Game();
        game.setCreator(creator);
        game.setName("Partida insertada");
        game.setTurn(1);
        game.setStartTime(LocalDateTime.now());
        game.setMaxNumberOfPlayers(3);
        assertThrows(NewGameException.class , () -> {gameService.createNewGame(game);});
    }

    @Test
    void testShouldNotCreateGameBecauseCreatorAlreadyInLobby() throws NewGameException{
        User creator = (User) userService.findUserById(25);//Un usuario en partida
        Game game = new Game();
        game.setCreator(creator);
        game.setName("Partida insertada");
        game.setTurn(1);
        game.setStartTime(LocalDateTime.now());
        game.setMaxNumberOfPlayers(3);
        assertThrows(NewGameException.class , () -> {gameService.createNewGame(game);});
    }
    
    
    @Test
    public void testOnePlayerShouldDie() throws DataAccessException, NotFoundException{
        Integer numberPlayers = 2; 
        Game game = gameService.findGameById(5);
        Player playerActualTurn = gameService.actualTurnPlayer(5);
        Integer numPlayersTest = game.getPlayers().size();
        assertEquals(numPlayersTest, numberPlayers);
        //Vamos a crear una tirada a mano para matar al player 1 
        Roll roll = new Roll();
        DiceValues[] guardados = new DiceValues[]{DiceValues.ATTACK,
            DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK};
        roll.setKeep(guardados);
        roll.setRollAmount(2);
        playerService.useRoll(playerActualTurn, roll);
        numberPlayers = numberPlayers-1;
        Integer numPlayersTest2 = game.playersAlive().size();
        assertEquals(numberPlayers, numPlayersTest2);
    }
    @Test
    void testShouldStartGame() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(6);//Un game sin empezar (pero que cumple las condiciones para empezar)
        try {
            gameService.startGame(game);
            assertNotNull(game.getStartTime());
            assertEquals(1, game.getTurn());
            Integer gamesThatHadStarted = gameService.findOnGoingGames().size();
            assertEquals(gamesStarted+1, gamesThatHadStarted);      
        } catch (NewGameException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }   
    }

    @Test
    void testStartGameNotEnoughPlayers() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(7);//Un game sin empezar con 1 solo jugador
        assertThrows(NewGameException.class, () -> {gameService.startGame(game);});
        Integer gamesThatHadStarted = gameService.findOnGoingGames().size();
        assertEquals(gamesStarted, gamesThatHadStarted);       
    }
    
    @Test
    void testStartGameAlreadyStarted() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(5);//Un game sin empezar con 1 solo jugador
        assertThrows(NewGameException.class, () -> {gameService.startGame(game);});  
        Integer gamesThatHadStarted = gameService.findOnGoingGames().size();
        assertEquals(gamesStarted, gamesThatHadStarted);     
    }
    
    @Test
    void testFindLobbies(){
        Integer calculatedGamesInLobby = gameService.findLobbies().size();
        assertEquals(gamesInLobby, calculatedGamesInLobby);     
    }

    @Test
    void testDeleteGame() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(6);//un game sin empezar
        try {
            gameService.deleteGame(game);
            assertThrows(NotFoundException.class,() ->{gameService.findGameById(6);});
            Integer calculatedGamesInLobby = gameService.findLobbies().size();
            assertEquals(gamesInLobby-1, calculatedGamesInLobby);      
        } catch (DeleteGameException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }   
    }

    @Test
    void testShouldNotDeleteGameStarted() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(5);//un game empezado
        assertThrows(DeleteGameException.class, () -> {gameService.deleteGame(game);});
        Integer calculatedGamesPlaying = gameService.findOnGoingGames().size();
        assertEquals(gamesStarted, calculatedGamesPlaying);    
    }

    @Test
    void testPlayerInGameByUser() throws DataAccessException, NotFoundException{
        Player player = playerService.findPlayerById(19);
        User user = userService.findUserById(21);
        Player playerObtenido = gameService.playerInGameByUser(user, 5);
        assertEquals(player, playerObtenido);
    }

    @Test 
    void testShouldNotFindPlayerInGame(){
        User user = userService.findUserById(21);
        assertNull(gameService.playerInGameByUser(user, 6));//Probamos en un game en el que no está
    }

    @Test 
    void testShouldNotFindPlayerBecauseNullUser(){
        User user = null;
        assertThrows(NullPointerException.class, () -> {gameService.playerInGameByUser(user, 6);});
    }

    @Test
    void testFindActualTurnPlayerId() throws NotFoundException{
        Integer actualId = 19;
        Integer idFound = gameService.actualTurnPlayerId(5);
        assertNotNull(idFound);
        assertEquals(actualId,idFound);
    }
    
    @Test
    void testFindCorrectOrder() throws NotFoundException{
        Player firstPlayer = playerService.findPlayerById(16);
        Player secondPlayer = playerService.findPlayerById(14);
        List<Integer> listTurns = new ArrayList<>();
        listTurns.add(16);listTurns.add(14);
        List<Player> listFound = gameService.playersOrder(listTurns);
        assertEquals(listFound.get(0),firstPlayer);
        assertEquals(listFound.get(1),secondPlayer);
    }

    @Test
    void testValidateNewTurn() {
        Roll roll = new Roll();//Roll que no ha acabado (isFinished=false)
        assertThrows(InvalidPlayerActionException.class, () -> {gameService.validateNewTurn(roll, Boolean.TRUE); });
    }

    @Test
    void testValidateRoll() {
        Roll rollKeep = new Roll();
        DiceValues[] rkeep= new DiceValues[]{DiceValues.ATTACK,DiceValues.ENERGY};
        rollKeep.setKeep(rkeep);
        Roll newRoll = new Roll();
        List<DiceValues> rollvalues= new ArrayList<>();
        rollvalues.add(DiceValues.ATTACK);
        rollvalues.add(DiceValues.ENERGY);
        rollvalues.add(DiceValues.ONE); rollvalues.add(DiceValues.ONE); rollvalues.add(DiceValues.ONE); rollvalues.add(DiceValues.ONE);
        newRoll.setValues(rollvalues);
        try{
            gameService.validateRoll(rollKeep, newRoll);
        }catch(InvalidPlayerActionException e){
            fail(e.getMessage());

        }
    }

    @Test
    void testValidateRollThrowsException() {
        Roll rollKeep = new Roll();
        DiceValues[] rkeep= new DiceValues[]{DiceValues.ATTACK,DiceValues.ENERGY};
        rollKeep.setKeep(rkeep);
        Roll newRoll = new Roll();
        List<DiceValues> rollvalues= new ArrayList<>();
        rollvalues.add(DiceValues.ONE); 
        rollvalues.add(DiceValues.ONE);
        rollvalues.add(DiceValues.ONE); rollvalues.add(DiceValues.ONE); rollvalues.add(DiceValues.ONE); rollvalues.add(DiceValues.ONE);
        newRoll.setValues(rollvalues);
        assertThrows(InvalidPlayerActionException.class, () -> {gameService.validateRoll(rollKeep, newRoll);});
    }

    @Test
    void testUseCardsEndTurn() throws DataAccessException, NotFoundException{
        Player player = playerService.findPlayerById(19);
        Integer pVictoriaInicio = player.getVictoryPoints();
        gameService.useCardsEndTurn(player);
        assertThat(player.getVictoryPoints()).isGreaterThan(pVictoriaInicio);
        assertEquals(5,player.getVictoryPoints()); // 30/6 =5 puntos de victoria
    }
    
    @Test
    void testUseCardsStartTurn() throws DataAccessException, NotFoundException{
        Player player = playerService.findPlayerById(19);
        Roll roll = mapGameRepository.getRoll(player.getGame().getId());
        Integer numDados = roll.getValues().size();
        gameService.useCardsStartTurn(player);
        Roll roll2 = mapGameRepository.getRoll(player.getGame().getId());
        Integer numDadosFinal = roll2.getValues().size();
        assertThat(numDadosFinal).isGreaterThan(numDados);
    }

    @Test
    void testNuevoTurno() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(4);
        Integer turnInicial = game.getTurn();
        gameService.nuevoTurno(game);
        assertThat(game.getTurn()).isGreaterThan(turnInicial);
    }

    @Test
    void testInitialTurnList() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(4);
        Integer numPlayers = game.getPlayers().size();
        gameService.initialTurnList(game);
        List<Integer> turno = mapGameRepository.getTurnList(4);
        assertEquals(numPlayers, turno.size()); 
    }

    @Test
    void testTurnRollInitial(){
        Roll roll = new Roll();
        mapGameRepository.rollMap.put(5, roll);
        Integer tiradasIniciales = mapGameRepository.rollMap.get(5).getRollAmount();
        gameService.turnRoll(roll, 5);
        assertThat(mapGameRepository.rollMap.get(5).getRollAmount()).isGreaterThan(tiradasIniciales);
    }

    @Test
    void testTurnRollSecondRoll(){
        Roll roll = new Roll();
        roll.setRollAmount(1);
        mapGameRepository.rollMap.put(5, roll);
        Integer tiradasIniciales = mapGameRepository.rollMap.get(5).getRollAmount();
        gameService.turnRoll(roll, 5);
        assertThat(mapGameRepository.rollMap.get(5).getRollAmount()).isGreaterThan(tiradasIniciales);
    }

    @Test
    void testTurnRollKeepingAllDices(){
        Roll roll = new Roll();
        roll.setRollAmount(1);
        DiceValues[] guardados = new DiceValues[]{DiceValues.ATTACK,
            DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK,DiceValues.ATTACK};
        roll.setKeep(guardados);
        mapGameRepository.rollMap.put(5, roll);
        Integer tiradasIniciales = mapGameRepository.rollMap.get(5).getRollAmount();
        gameService.turnRoll(roll, 5);
        assertThat(mapGameRepository.rollMap.get(5).getRollAmount()).isGreaterThan(tiradasIniciales);
    }

    @Test
    void testHandleTurnActionWhenIsNotPlayersTurn() throws DataAccessException, NotFoundException, InvalidPlayerActionException{
        Game game = gameService.findGameById(5);
        Roll roll = new Roll();
        mapGameRepository.rollMap.put(5, roll);
        Integer tiradasIniciales = mapGameRepository.rollMap.get(5).getRollAmount();
        gameService.handleTurnAction(game, Boolean.FALSE, roll);
        assertEquals(tiradasIniciales,mapGameRepository.rollMap.get(5).getRollAmount());
    }

    @Test
    void testEndGameWithVictoryPoints() throws DataAccessException, NotFoundException{
        Integer gamesPlaying = numberOfGames-gameFinished;
        Game game = gameService.findGameById(4);
        Player player = gameService.actualTurn(4);
        Integer gamesPlayingCalculated = gameService.findAllNotFinished().size();// Los juegos sin empezar cuentan como juegos jugandose
        Integer finishedGamesCalculated = gameService.findAllFinished().size(); 
        assertEquals(gamesPlaying ,gamesPlayingCalculated);
        assertEquals(gameFinished,finishedGamesCalculated);
        player.setVictoryPoints(20);
        playerService.savePlayer(player);
        gameService.endGame(game);
        Integer finishedGamesCalculated2 = gameService.findAllFinished().size();
        assertEquals(gameFinished+1, finishedGamesCalculated2 );
        Integer gamesPlayingCalculated2 = gameService.findAllNotFinished().size();
        assertThat(gamesPlaying).isGreaterThan(gamesPlayingCalculated2);
    }

    @Test
    void testEndGameKillingEveryone() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(5);
        Player player = gameService.actualTurn(5);
        player.setLifePoints(0);
        playerService.savePlayer(player);
        gameService.endGame(game);
        Integer finishedGamesCalculated = gameService.findAllFinished().size();
        assertEquals(gameFinished+1, finishedGamesCalculated ); //Debería haber un game finished más que antes
        Integer gamesPlayingCalculated = gameService.findAllNotFinished().size();
        //Vamos a comprobar que el numero de partidas jugando ahora es 1 menos que antes
        assertThat(gamesPlaying).isGreaterThan(gamesPlayingCalculated); 
    }
    @Test
    void testShouldNotEndGame() throws DataAccessException, NotFoundException{
        Game game = gameService.findGameById(5);
        gameService.endGame(game);
        Integer finishedGamesCalculated = gameService.findAllFinished().size();
        assertEquals(gameFinished, finishedGamesCalculated ); //No debería acabar el juego
        Integer gamesPlayingCalculated = gameService.findAllNotFinished().size();
        assertEquals(gamesPlaying,gamesPlayingCalculated); 
    }

}
