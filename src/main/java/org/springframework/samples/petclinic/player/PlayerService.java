package org.springframework.samples.petclinic.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardEnum;
import org.springframework.samples.petclinic.card.CardType;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ricardo Nadal Garcia
 * @author Noelia López Durán
 */

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerStatusRepository playerStatusRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    @Transactional
    public Iterable<Player> findAll() {
        Iterable<Player> res = playerRepository.findAll();
        return res;
    }

    @Transactional
    public int playerCount() {
        return (int) playerRepository.count();
    }

    @Transactional
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    /* Esto actualmente no sirve para nada asi que ya me direis, era para lo de Noelia que hizo del throwExceptionDuplicatedMonsterName
    @Transactional
    public Player getPlayerwithIdDifferent(String monsterName, Integer id) {
        monsterName = monsterName.toLowerCase();
        for (Player player : playerRepository.findAll()) {
            String compName = player.getMonsterName().toString();
            compName = compName.toLowerCase();
            if (compName.equals(monsterName) && player.getId() != id) {
                return player;
            }
        }
        return null;
    }

    */


    /**
     * Join user to a game associating a new player to both. A player can only join
     * a game if the game has not started, has room for new players and the user is
     * not currently playing other game
     * 
     * @param user
     * @param newPlayer monsterName cannot be null
     * @param game
     */
    @Transactional
    public void joinGame(User user, Player newPlayer, Game game) {
        Monster monster = newPlayer.getMonster();
        if (game.hasRoom() && 
        !game.isStarted() && 
        game.monsterAvailable(monster) &&
         !user.hasActivePlayer()
                && monster != null) {

            newPlayer.setGame(game);
            newPlayer.setUser(user);
            newPlayer.setEnergyPoints(0);
            newPlayer.setLifePoints(10);
            newPlayer.setVictoryPoints(0);
            newPlayer.setLocation(LocationType.fueraTokyo);

            savePlayer(newPlayer);
        }
    }

    @Transactional
    public Player findPlayerById(int id) throws DataAccessException {
        return playerRepository.findById(id).get();
    }

    @Transactional
    public void savePlayerStatus(PlayerStatus pStatus) throws DataAccessException {
        playerStatusRepository.save(pStatus);
    }

    @Transactional
    public List<PlayerStatus> findPlayerStatus(int playerId) {
        return playerStatusRepository.findByPlayerId(playerId);

    }

    @Transactional(readOnly = true)
    public List<StatusType> findStatusTypes() throws DataAccessException {
        List<StatusType> ct = new ArrayList<StatusType>();
        ct.add(StatusType.Veneno);
        ct.add(StatusType.Reductor);
        return ct;
    }




    @Transactional
    public void useRoll(int gameId, Integer playerIdActualTurn, Roll roll) {
        Player playerActualTurn = findPlayerById(playerIdActualTurn);
        List<Player> listaJugadoresEnPartida=playerActualTurn.getGame().getPlayers();
        
        Boolean tokyoCityEmpty = Boolean.FALSE;
        Boolean tokyoBayEmpty = Boolean.FALSE;

        useCards(playerActualTurn);

        Map<String,Integer> rollCount=countRollValues(roll.getValues());
        Map<String,Integer> cardValuesCount=countRollValues(roll.getCardExtraValues());

        Integer heal = rollCount.get("heal") + cardValuesCount.get("heal");
        Integer damage = rollCount.get("damage") + cardValuesCount.get("damage");
        Integer energys = rollCount.get("energy") + cardValuesCount.get("energy");
        Integer ones =rollCount.get("ones") + cardValuesCount.get("ones");
        Integer twos = rollCount.get("twos") + cardValuesCount.get("twos");
        Integer threes = rollCount.get("threes") +  cardValuesCount.get("threes");


        // Si tokyo tiene espacio
        Boolean bayInPlay = listaJugadoresEnPartida.stream().filter(p -> !p.isDead()).count() > 4;
        tokyoCityEmpty = !listaJugadoresEnPartida.stream()
                .anyMatch(p -> p.getLocation().equals(LocationType.ciudadTokyo));
        tokyoBayEmpty = !listaJugadoresEnPartida.stream()
                .anyMatch(p -> p.getLocation().equals(LocationType.bahiaTokyo));

        if (tokyoCityEmpty && damage > 0) {
            playerActualTurn.setLocation(LocationType.ciudadTokyo);
            playerActualTurn.setVictoryPoints(playerActualTurn.getVictoryPoints() + 1);
            damage--;
        } else if (bayInPlay && tokyoBayEmpty && damage > 0) {
            playerActualTurn.setLocation(LocationType.bahiaTokyo);
            playerActualTurn.setVictoryPoints(playerActualTurn.getVictoryPoints() + 1);
            damage--;
        }
        // Los efectos de los dados
        for (Player player : listaJugadoresEnPartida) {
            if (playerIdActualTurn == player.getId()) {
                // CURACION
                if (player.getLocation() == LocationType.fueraTokyo) {
                    healDamage(player, heal);
                }
                // ENERGIAS
                Integer sumaEnergias = player.getEnergyPoints() + energys;
                player.setEnergyPoints(sumaEnergias);

                // PUNTUACION
                Integer totalPoints=calculatePoints(ones, twos, threes);
                player.setVictoryPoints(player.getVictoryPoints() + totalPoints);

            } else {
                // Daño a los otros jugadores estando fuera de tokyo
                if (playerActualTurn.getLocation() == LocationType.fueraTokyo) {
                    if (player.getLocation() == LocationType.ciudadTokyo || player.getLocation() == LocationType.bahiaTokyo) {
                        damagePlayer(player, damage);
                        if(damage>=1){ //Si se hace daño a otros jugadores
                            player.setRecentlyHurt(Boolean.TRUE);
                        }
                    }
                    // Daño a otros jugadores estando en Tokyo (ciudad o bahía)
                } else if (playerActualTurn.getLocation() == LocationType.bahiaTokyo
                        || playerActualTurn.getLocation() == LocationType.ciudadTokyo) {
                    if (player.getLocation() == LocationType.fueraTokyo) {
                        damagePlayer(player, damage);
                    }
                }
            }
            savePlayer(player);
        }
    }


   public void useCards(Player player) {
        for(Card card:player.getAvailableCards()) {
            if(card.getType() != CardType.DESCARTAR) {
            card.getCardEnum().effect(player, playerService);
            }
        }
    }




@Transactional
   public Map<String,Integer> countRollValues(List<DiceValues> values){
    Integer heal = 0;
    Integer damage = 0;
    Integer energys = 0;
    Integer ones = 0;
    Integer twos = 0;
    Integer threes = 0;
    Map<String,Integer> rollValues=new HashMap<String,Integer>();

    for (DiceValues valorDado : values) {
        switch (valorDado) { // Lo estoy dejando de esta manera tan extensa por si luego hay que tener en
                             // cuenta las cartas para cada tipo de dado
        case HEAL:
            heal++;
            break;
        case ATTACK:
            damage++;
            break;
        case ENERGY:
            energys++;
            break;
        case ONE:
            ones++;
            break;
        case TWO:
            twos++;
            break;
        case THREE:
            threes++;
            break;
        }
    }
    rollValues.put("heal", heal);
    rollValues.put("damage", damage);
    rollValues.put("energy", energys);
    rollValues.put("ones", ones);
    rollValues.put("twos", twos);
    rollValues.put("threes", threes);

    return rollValues;
   }

    @Transactional
    public void healDamage(Player player, Integer healPoints) {
        healPoints=player.getLifePoints()+healPoints;
        Integer playerMaxHealth=player.getMaxHealth();
        if (healPoints <= playerMaxHealth) {
            player.setLifePoints(healPoints);
        } else {
            player.setLifePoints(playerMaxHealth);
        }
    }

    @Transactional
    public Integer calculatePoints(Integer ones,Integer twos, Integer threes) {
        Integer result = 0;
        Integer sumOnes = (ones - 2);
        if (sumOnes > 0) {
            result += sumOnes;
            }
        Integer sumTwos = (twos - 1);
        if (twos - 2 > 0) {
            result += sumTwos;
        }
        Integer sumThrees = threes;
        if (threes - 2 > 0) {
            result += sumThrees;
        }
        return result;
    }

    @Transactional
    public void damagePlayer(Player player, Integer damage) {
        Integer damagedLife = player.getLifePoints() - damage;
        if (0 < damagedLife) {
            player.setLifePoints(damagedLife);
        } else {
            player.setLifePoints(0);
            player.setLocation(LocationType.fueraTokyo);
        }
    }

    @Transactional
    public void substractVictoryPointsPlayer(Player player, Integer victoryPoints) {
        Integer victoryPointsNew = player.getLifePoints() - victoryPoints;
        if (0 < victoryPointsNew) {
            player.setVictoryPoints(victoryPointsNew);
        } else {
            player.setVictoryPoints(0);
        }
    }

    @Transactional
    public void startTurn(Integer playerId) {
        Player player = findPlayerById(playerId);
        if (player.getLocation().equals(LocationType.ciudadTokyo)
                || player.getLocation().equals(LocationType.bahiaTokyo)) {

            player.setVictoryPoints(player.getVictoryPoints() + 2);
            
            savePlayer(player);
        }
        
    }

    @Transactional
    public Player actualPlayer(Integer gameId) {
        Game game = gameService.findGameById(gameId);
        User user = userService.authenticatedUser();
        return game.getPlayers().stream().filter(p -> p.getUser().getId().equals(user.getId())).findAny().get();
    }

    @Transactional
    public void surrender(Integer playerId) {
        Player player = findPlayerById(playerId);
        User user = userService.authenticatedUser();
        if (player.getUser().getId() == user.getId()) {
            player.surrender();
            gameService.endGame(player.getGame().getId());
            savePlayer(player);
        }
    }
    /**
     * @return True if the player has been hurt (the property recentlyhurt of the player equals true)
     */
    public Boolean isRecentlyHurt(Integer gameId){
        User user = userService.authenticatedUser();
        Player player = gameService.playerInGameByUser(user, gameId);
        Boolean result = Boolean.FALSE;
        if(player.getRecentlyHurt()==Boolean.TRUE){
            result = Boolean.TRUE;
        }
        return result;
    }
    /**
     * @return True if the player is in TokyoCity or TokyoBay.
     */
    public Boolean isInTokyo(Integer gameId){
        User user = userService.authenticatedUser();
        Player player = gameService.playerInGameByUser(user, gameId);
        Boolean result = Boolean.FALSE;
        if(player.getLocation()==LocationType.bahiaTokyo || player.getLocation() == LocationType.ciudadTokyo){
            result = Boolean.TRUE;
        }
        return result;
    }

   
}