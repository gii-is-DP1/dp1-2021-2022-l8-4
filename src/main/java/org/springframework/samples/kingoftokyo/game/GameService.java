package org.springframework.samples.kingoftokyo.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

import javassist.NotFoundException;

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
    

    private String laPartida = "La partida {id: '";
    private String name = "', Name: '";

    @Transactional
    public Iterable<Game> findAll() {
        return gameRepository.findAll();
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
    public Game findGameById(int id) throws DataAccessException, NotFoundException {
        Optional<Game> game = gameRepository.findById(id);
        if(!game.isEmpty()){
            return game.get();
        }else{
            throw new NotFoundException("Game {id:"+id+"} no encontrada");
        }
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
            throw new DeleteGameException(laPartida + game.getId() + name + game.getName()+"'} no puede ser borrada");
        }
    }

    /**
     * @return Associated player if exist in the game or null if not
     */
    @Transactional
    public Player playerInGameByUser(User user, int gameId) {
        return user.getPlayers().stream().filter(p -> p.getGame().getId() == gameId).findFirst().get();
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
                throw new NewGameException(laPartida + game.getId() + name + game.getName()+"'} no puede ser iniciada porque no hay suficientes jugadores");
            }else{
                throw new NewGameException(laPartida + game.getId() + name + game.getName()+"'} ya ha sido iniciada");
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
            throw new NewGameException("El usuario {id: '"+creator.getId()+"', Username: '"+creator.getUsername()+"'} ya tiene otro juego activo");
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
    public void nuevoTurno(Game game) throws NotFoundException {
        Integer gameId = game.getId();
        mapGameRepository.putRoll(gameId, new Roll());

        game.setTurn(game.getTurn() + 1);

        nextPositionTurn(gameId);

        useCardsStartTurn(actualTurn(gameId));
        saveGame(game);
        playerService.startTurn(actualTurnPlayer(gameId));
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
    public void nextPositionTurn(Integer gameId) throws DataAccessException, NotFoundException {
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
    public Integer actualTurnPlayerId(Integer gameId) throws NotFoundException {
        return actualTurnPlayer(gameId).getId();
    }

    @Transactional
    public Player actualTurnPlayer(Integer gameId) throws NotFoundException {
        Player player = actualTurn(gameId);
        return player;
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
    public void endGame(Game game) {
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
    public List<Integer> initialTurnList(Game game) {
        List<Integer> listaTurnos = new ArrayList<Integer>();
        List<Player> jugadores = game.getPlayers();
        for (Player player : jugadores) {
            listaTurnos.add(player.getId());
        }
        Collections.shuffle(listaTurnos);
        return listaTurnos;
    }

    /**
     * Given a gameId returns the player who is playing their turn
     * @param game
     * @throws NotFoundException
     * @throws DataAccessException
     */
    @Transactional
    public Player actualTurn(Integer gameId) throws NotFoundException {

        List<Integer> turnList = mapGameRepository.getTurnList(gameId);
        Player actualPlayer = playerService.findPlayerById(turnList.get(0));

        return actualPlayer;
    }

    @Transactional
    public Boolean isPlayerTurn(Integer gameId) throws NotFoundException {
        User user = userService.authenticatedUser();
        Boolean result = Boolean.FALSE;
        if (user != null) {
            result = actualTurn(gameId).getUser().getId().equals(user.getId());
        }
        return result;
    }

    @Transactional
    public Boolean isPlayerInGame(Game game) {
        User user = userService.authenticatedUser();
        Boolean result = Boolean.FALSE;
        if (user != null) {
            for (Player player : game.getPlayers()) {
                result = result || player.getUser().getId().equals(user.getId());
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
    public void handleTurnAction(Game game, Boolean newTurn, Roll keepInfo) throws NotFoundException {
        Integer gameId = game.getId();
        if (isPlayerTurn(gameId)) {
            if (newTurn) {
                useCardsEndTurn(playerService.actualPlayer(game));
                isRecentlyHurtToFalse(game);
                nuevoTurno(game);
                playerService.checkplayers(game);
            } else {
                Roll rollData = mapGameRepository.getRoll(gameId); 
                rollData.setKeep(keepInfo.getKeep());
                turnRoll(rollData, gameId);
                if (rollData.getRollAmount().equals(rollData.getMaxThrows())) {
                    Player playerActualTurn = actualTurnPlayer(gameId);
                    playerService.useRoll(playerActualTurn, rollData);

                }
            }
        }

    }
    /**
     * Swaps the locations between the player who wants to leave tokyo and the player
     * who hurt the player in tokyo.
     * @param game
     * @throws NotFoundException
     */
    @Transactional
    public void changePosition(Integer gameId) throws NotFoundException {
        Player playerActualTurn = actualTurnPlayer(gameId);
        User user = userService.authenticatedUser();
        Player player = playerInGameByUser(user, gameId);
        LocationType LeavingTokyoLocation = player.getLocation();
        playerActualTurn.setLocation(LeavingTokyoLocation);
        player.setLocation(LocationType.fueraTokyo);
        playerService.savePlayer(player);
        playerService.savePlayer(playerActualTurn);
    }
    /**
     * Changes the attribute "recentlyHurt" of all the players in the game to FALSE 
     * @param game
     */
    @Transactional
    public void isRecentlyHurtToFalse(Game game) {
        List<Player> lsplayer = game.getPlayers();
        for (Player player : lsplayer) {
            player.setRecentlyHurt(Boolean.FALSE);
            playerService.savePlayer(player);
        }
    }

    /**
     * Handles the changes of the attribute "Location" of a player from Tokyo(or TokyoBay) to fueraDeTokyo 
     * if the players has been hurt and is on Tokyo(or Tokyobay) 
     * @param game
     * @throws NotFoundException
     */
    @Transactional
    public void handleExitTokyo(Game game) throws NotFoundException {
        if (playerService.isRecentlyHurt(game.getId()) && playerService.isInTokyo(game.getId())) {
            changePosition(game.getId());
            isRecentlyHurtToFalse(game);
        }
    }

    /**
     * Given a list with the players Id (in order) returns a list with the Players in the given order
     * @param turnList
     * @return list of players
     */
    @Transactional
    public List<Player> playersOrder(List<Integer> turnList) {
        return turnList.stream().map(id -> {
            try {
                return playerService.findPlayerById(id);
            } catch (DataAccessException | NotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }
}
