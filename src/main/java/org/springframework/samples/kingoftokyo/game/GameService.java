package org.springframework.samples.kingoftokyo.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.card.CardService;
import org.springframework.samples.kingoftokyo.card.CardType;
import org.springframework.samples.kingoftokyo.dice.DiceValues;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.game.exceptions.DeleteGameException;
import org.springframework.samples.kingoftokyo.game.exceptions.NewGameException;
import org.springframework.samples.kingoftokyo.gamecard.GameCardService;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.samples.kingoftokyo.player.LocationType;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 * @author Ricardo Nadal Garcia
 */
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CardService cardService;

    @Autowired
    private GameCardService gameCardService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private MapGameRepository mapGameRepository;
    

    @Transactional
    public Iterable<Game> findAll() {
        Iterable<Game> res = gameRepository.findAll();
        return res;
    }

    @Transactional
    public int gameCount() {
        return (int) gameRepository.count();
    }

    @Transactional
    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    @Transactional
    public Game findGameById(int id) throws DataAccessException {
        return gameRepository.findById(id).get();
    }

    @Transactional
    public List<Player> findPlayerList(int gameId) throws DataAccessException {
        return gameRepository.findById(gameId).get().getPlayers();
    }

    @Transactional
    public List<Game> findOnGoingGames() throws DataAccessException {
        return gameRepository.findOnGoingGames();
    }

    /**
     * @return List of games that have not started yet
     */
    @Transactional
    public List<Game> findLobbies() {
        return gameRepository.findLobbies();
    }

    /**
     * Delete a game given its creator
     */
    @Transactional
    public void deleteGame(Game game) throws DeleteGameException {
        if (!game.isStarted()) {
            gameRepository.delete(game);
        }else{
            throw new DeleteGameException();
        }
    }

    /**
     * @return Associated player if exist in the game or null if not
     */
    @Transactional
    public Player playerInGameByUser(User user, int gameId) {
        Player player = user.getPlayers().stream().filter(p -> p.getGame().getId() == gameId).findFirst().get();
        return player;
    }

    /**
     * Start the game if has enough players and the game was not previously started
     */
    @Transactional
    public void startGame(Game game) throws NewGameException {
        if (game.hasEnoughPlayers() && !game.isStarted()) {
            game.setTurn(1);
            game.setStartTime(LocalDateTime.now());

            cardService.newDeck(game);
            gameCardService.showCards(game);

            saveGame(game);
        }else{
            if(!game.hasEnoughPlayers()){
                throw new NewGameException("La partida no puede ser iniciada porque no hay suficientes jugadores");
            }else{
                throw new NewGameException("La partida ya ha sido iniciada");
            }
            
        }
    }

    /**
     * Create a new game
     * @param game
     * @throws NewGameException
     */
    @Transactional
    public void createNewGame(Game game) throws NewGameException{
        User creator = game.getCreator();
        if(creator.hasActiveGameAsCreator() || creator.hasActivePlayer()){
            throw new NewGameException("El usuario ya tiene otro juego activo");
        }else{
            saveGame(game);
        }
    }

    @Transactional
    public void turnRoll(Roll roll, Integer gameId) {
        if (roll.getRollAmount() == null || roll.getRollAmount() == 0) {
            roll.rollDiceInitial();

        } else if (roll.getRollAmount() < roll.getMaxThrows() && roll.getKeep().length < roll.getValues().size()) {
            List<DiceValues> valoresConservados = Arrays.asList(roll.getKeep());
            roll.rollDiceNext(valoresConservados);
        } else if (roll.getRollAmount() < roll.getMaxThrows()) {
            List<DiceValues> valoresConservados = Arrays.asList(roll.getKeep());
            roll.rollDiceNext(valoresConservados);
            roll.setRollAmount(roll.getMaxThrows());
        }
        mapGameRepository.putRoll(gameId, roll);
    }

    @Transactional
    public void nuevoTurno(int gameId) {
        Game game = findGameById(gameId);
        mapGameRepository.putRoll(gameId, new Roll());

        game.setTurn(game.getTurn() + 1);

        nextPositionTurn(gameId);

        useCardsStartTurn(actualTurn(gameId));
        saveGame(game);
        playerService.startTurn(actualTurnPlayerId(gameId));
    }

    @Transactional
    public void useCardsStartTurn(Player player) {
        for (Card card : player.getAvailableCards()) {
            if (card.getType() != CardType.DESCARTAR) {
                card.getCardEnum().effectStartTurn(player, playerService,mapGameRepository);
            }
        }
    }

    @Transactional
    public void nextPositionTurn(Integer gameId) {
        List<Integer> turnList = mapGameRepository.getTurnList(gameId);

        Boolean finished = Boolean.FALSE;

        while (!finished) {
            Player player = playerService.findPlayerById(turnList.get(1));
            if (!player.isDead()) {
                turnList.add(turnList.remove(0));
                mapGameRepository.putTurnList(gameId, turnList);
                break;
            } else {
                turnList.remove(1);
            }
        }

    }

    @Transactional
    public Integer actualTurnPlayerId(Integer gameId) {
        Player player = actualTurn(gameId);
        return player.getId();
    }

    @Transactional
    public List<Game> findAllFinished() {
        Iterable<Game> resultSinFiltrar = findAll();
        List<Game> resultadoFiltrado = new ArrayList<Game>();
        for (Game game : resultSinFiltrar) {
            if (game.isFinished()) {
                resultadoFiltrado.add(game);
            }
        }
        return resultadoFiltrado;
    }

    @Transactional
    public List<Game> findAllNotFinished() {
        Iterable<Game> resultSinFiltrar = findAll();
        List<Game> resultadoFiltrado = new ArrayList<Game>();
        for (Game game : resultSinFiltrar) {
            if (!game.isFinished()) {
                resultadoFiltrado.add(game);
            }
        }
        return resultadoFiltrado;
    }

    @Transactional
    public void endGame(Integer gameId) {
        Game game = findGameById(gameId);
        if (game.playersWithMaxVictoryPoints().size() != 0) {
            game.setWinner(game.playersWithMaxVictoryPoints().get(0).getUser().getUsername());
            game.setEndTime(LocalDateTime.now());
        } else if (game.playersAlive().size() == 1) {
            game.setEndTime(LocalDateTime.now());
            game.setWinner(game.playersAlive().get(0).getUser().getUsername());
        }

        // Check achievements for every user when the game is finished
        if (game.isFinished()) {
            game.getPlayers().stream()
                    .forEach(p -> achievementService.checkAchievements(p.getUser()));
        }
        saveGame(game);
    }

    @Transactional
    public List<Integer> initialTurnList(Integer gameId) {
        List<Integer> listaTurnos = new ArrayList<Integer>();
        List<Player> jugadores = findPlayerList(gameId);
        for (Player player : jugadores) {
            listaTurnos.add(player.getId());
        }
        Collections.shuffle(listaTurnos);
        return listaTurnos;
    }

    @Transactional
    public Player actualTurn(Integer gameId) {

        List<Integer> turnList = mapGameRepository.getTurnList(gameId);
        Player actualPlayer = playerService.findPlayerById(turnList.get(0));

        return actualPlayer;
    }

    @Transactional
    public Boolean isPlayerTurn(Integer gameId) {
        User user = userService.authenticatedUser();
        Boolean result = Boolean.FALSE;
        if (user != null) {
            result = actualTurn(gameId).getUser().getId() == user.getId();
        }
        return result;
    }

    @Transactional
    public Boolean isPlayerInGame(Integer gameId) {
        Game game = findGameById(gameId);
        User user = userService.authenticatedUser();
        Boolean result = Boolean.FALSE;
        if (user != null) {
            for (Player player : game.getPlayers()) {
                result = result || player.getUser().getId() == user.getId();
            }
        }
        return result;
    }

    @Transactional
    public void useCardsEndTurn(Player player) {
        for (Card card : player.getAvailableCards()) {
            card.getCardEnum().effectEndTurn(player, playerService,mapGameRepository);
        }
    }

    @Transactional
    public void handleTurnAction(Integer gameId, Boolean newTurn, Roll keepInfo) {
        if (isPlayerTurn(gameId)) {
            if (newTurn) {
                useCardsEndTurn(playerService.actualPlayer(gameId));
                isRecentlyHurtToFalse(gameId);
                nuevoTurno(gameId);
                checkPlayersAlive(gameId);
                playerService.checkplayers(gameId);
            } else {
                Roll rollData = mapGameRepository.getRoll(gameId); 
                rollData.setKeep(keepInfo.getKeep());
                turnRoll(rollData, gameId);
                if (rollData.getRollAmount() == rollData.getMaxThrows()) {
                    Integer playerIdActualTurn = actualTurnPlayerId(gameId);
                    playerService.useRoll(playerIdActualTurn, rollData);

                }
            }
        }

    }
    /**
     * Checks is the number of player is less than 5 and disables Tokyo Bay. 
     * @param gameId
     */
    @Transactional
    public void checkPlayersAlive(Integer gameId){
        Game game  = findGameById(gameId);
        List<Player> players = game.playersAlive();
        if(players.size()<5){
            for(Player player : players){
                if(player.getLocation()==LocationType.bahiaTokyo){
                    player.setLocation(LocationType.fueraTokyo);
                }
            }
        }
    }

    @Transactional
    public void changePosition(Integer gameId) {
        Player playerActualTurn = playerService.findPlayerById(actualTurnPlayerId(gameId));
        User user = userService.authenticatedUser();
        Player player = playerInGameByUser(user, gameId);
        LocationType LeavingTokyoLocation = player.getLocation();
        playerActualTurn.setLocation(LeavingTokyoLocation);
        player.setLocation(LocationType.fueraTokyo);
        playerService.savePlayer(player);
        playerService.savePlayer(playerActualTurn);
    }

    @Transactional
    public void isRecentlyHurtToFalse(Integer gameId) {
        List<Player> lsplayer = findPlayerList(gameId);
        for (Player player : lsplayer) {
            player.setRecentlyHurt(Boolean.FALSE);
            playerService.savePlayer(player);
        }
    }

    @Transactional
    public void handleExitTokyo(Integer gameId) {
        if (playerService.isRecentlyHurt(gameId) && playerService.isInTokyo(gameId)) {
            changePosition(gameId);
            isRecentlyHurtToFalse(gameId);
        }
    }

    /**
     * Given a list with the players Id (in order) returns a list with the Players in the given order
     * @param turnList
     * @return list of players
     */
    @Transactional
    public List<Player> playersOrder(List<Integer> turnList) {
        return turnList.stream().map(id -> playerService.findPlayerById(id)).collect(Collectors.toList());
    }
}
