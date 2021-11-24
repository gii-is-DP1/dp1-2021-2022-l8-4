package org.springframework.samples.petclinic.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
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
    
    @Transactional
    public Iterable<Game> findAll(){
        Iterable<Game> res = gameRepository.findAll();
        return res;
    }

    @Transactional
    public int gameCount(){
        return (int) gameRepository.count();
    }

    @Transactional
    public void saveGame(Game game){
        gameRepository.save(game);
    }

    @Transactional
    public Game findGameById(int id) throws DataAccessException{
        return gameRepository.findById(id).get();
    }

    @Transactional
    public List<Player> findPlayerList(int gameId) throws DataAccessException{
        return gameRepository.findById(gameId).get().getPlayers();
    }

    @Transactional
    public List<Game> findOnGoingGames() throws DataAccessException{
        return gameRepository.findOnGoingGames();
    }

    /**
	 * @return List of games that have not started yet
	 */
    @Transactional
    public List<Game> findLobbies(){
        return gameRepository.findLobbies();
    }
    

    /**
	 * Create a new game in the data base setting its creator
	 */
    @Transactional
    public void createNewGame(User creator, Game newGame){
        newGame.setCreator(creator);
        newGame.setTurn(0);
        saveGame(newGame);
    }

    /**
     * Delete a game given its creator
	 */
    @Transactional
    public void deleteGameByCreator(User creator, Game game){
        if(creator.isCreator(game)){
            gameRepository.delete(game);
        }  
    }

    /**
	 * @return Associated player if exist in the game or null if not
	 */
    @Transactional
    public Player playerInGameByUser(User user, int gameId){
        Player player = user.getPlayers().stream()
                        .filter(p -> p.getGame().getId() == gameId)
                        .findFirst()
                        .get();
        return player;
    }

    /**
	 * @return True if the game could be started, false if the game cannot be started yet
	 */
    public Boolean startGameByCreator(User creator, Game game){
        Boolean started=false;
        if(creator.isCreator(game) && game.hasEnoughPlayers() && !game.isStarted()){
            game.setTurn(1);
            game.setStartTime(LocalDateTime.now());

            //Here we should initialize the deck order

            saveGame(game);
            started=true;
        }
        return started;
    }

    @Transactional
    public void turnRoll(Roll roll,Integer gameId) {
        if(roll.getRollAmount() == null || roll.getRollAmount() == 0) {
            roll.rollDiceInitial();
        } else if(roll.getRollAmount() < roll.getMaxThrows() && roll.getKeep().length != 6) {
            List<DiceValues> valoresConservados=Arrays.asList(roll.getKeep());
            roll.rollDiceNext(valoresConservados);
        } else if (roll.getRollAmount() < roll.getMaxThrows()){
            List<DiceValues> valoresConservados=Arrays.asList(roll.getKeep());
            roll.rollDiceNext(valoresConservados);
            roll.setRollAmount(roll.getMaxThrows()); 
        }
        MapGameRepository.getInstance().putRoll(gameId,roll);
    }

    public void nuevoTurno(int gameId) {
        Game game=findGameById(gameId);
        MapGameRepository.getInstance().putRoll(gameId, new Roll());
        game.setTurn(game.getTurn()+1);
        saveGame(game);
        playerService.startTurn(actualTurnPlayerId(gameId));
    }

    public Integer actualTurnPlayerId(Integer gameId){
        Player player=actualTurn(gameId);
        return player.getId();
    }

    public List<Game> findAllFinished() {
        Iterable<Game> resultSinFiltrar=findAll();
        List<Game> resultadoFiltrado=new ArrayList<Game>();
        for(Game game:resultSinFiltrar) {
            if(game.isFinished()) {
                resultadoFiltrado.add(game);
            }
        }
        return resultadoFiltrado;
    }

    public List<Game> findAllNotFinished() {
        Iterable<Game> resultSinFiltrar=findAll();
        List<Game> resultadoFiltrado=new ArrayList<Game>();
        for(Game game:resultSinFiltrar) {
            if(!game.isFinished()) {
                resultadoFiltrado.add(game);
            }
        }
        return resultadoFiltrado;
    }

    public void endGame(Integer gameId) {
        Game game = findGameById(gameId);
        if(game.playersWithMaxVictoryPoints().size() != 0) {
            game.setWinner(game.playersWithMaxVictoryPoints().get(0).getUser().getUsername());
            
        } else if(game.playersAlive().size() == 1){
            
             game.setWinner(game.playersAlive().get(0).getUser().getUsername()); 
        }
        saveGame(game);
    }



    public List<Integer> initialTurnList(Integer gameId){
        List<Integer> listaTurnos=new ArrayList<Integer>();
        List<Player> jugadores= findPlayerList(gameId);
        for(Player player:jugadores) {
           listaTurnos.add(player.getId());
        }
        Collections.shuffle(listaTurnos);
        return listaTurnos;
     }

     
    public Player actualTurn(Integer gameId){
        List<Integer> turnList=MapGameRepository.getInstance().getTurnList(gameId);
        Game game = findGameById(gameId);
        List<Player> jugadores=game.getPlayers();
        Player actualPlayer= actualTurnPositionList(turnList, game.getTurn(), jugadores);
        
        return actualPlayer;
    }

    private Player actualTurnPositionList(List<Integer> turnList,Integer listPosition,List<Player> players) {
        Integer turnNum = listPosition % (players.size());
        
        for(Player player:players) {
           if(player.getId()==turnList.get(turnNum) && player.isDead()) {
              turnNum++;
              return actualTurnPositionList(turnList, turnNum, players);
           } else if(player.getId()==turnList.get(turnNum)){
              return player;
           }
        }
        return null;
    }

    @Transactional
    public Boolean isPlayerTurn(Integer gameId){
        User user=userService.authenticatedUser();
        Boolean result=Boolean.FALSE;
        if(user!=null){
            result=actualTurn(gameId).getUser().getId() == user.getId();
        }
        return result;
    }

    @Transactional
    public Boolean isPlayerInGame(Integer gameId) {
        Game game = findGameById(gameId);
        User user=userService.authenticatedUser();
        Boolean result=Boolean.FALSE;
        if(user!=null) {
            for(Player player:game.getPlayers()) {
                result=result || player.getUser().getId() == user.getId();
            }
        }
        return result;
    }


    
    

    

     

   
}
