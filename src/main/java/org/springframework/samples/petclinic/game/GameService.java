package org.springframework.samples.petclinic.game;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.user.User;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 * @author Ricardo Nadal Garcia
 */
@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;

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

    @Transactional
    public void createNewGame(User creator, Game newGame){
        newGame.setCreator(creator);
        newGame.setTurn(0);
        saveGame(newGame);
    }

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
            started=true;
        }
        return started;
    }

    @Transactional
    public void turnRoll(Roll roll) {
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
    }

    public void nuevoTurno(int gameId) {
        Game game=findGameById(gameId);
        game.setTurn(game.getTurn()+1);
        saveGame(game);
    }

    public Integer actualTurnPlayerId(List<Integer> turnList,Integer gameId){
        Game game=findGameById(gameId);
        Player player=game.actualTurn(turnList);
        return player.getId();
    }

    
    

    

     

   
}
