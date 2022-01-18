package org.springframework.samples.kingoftokyo.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.dice.DiceValues;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.game.GameService;
import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.samples.kingoftokyo.game.exceptions.NewGameException;
import org.springframework.samples.kingoftokyo.playercard.PlayerCard;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javassist.NotFoundException;

/**
 * @author Ricardo Nadal Garcia
 * @author Noelia López Durán
 */

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService pService;
    @Autowired
    private MapGameRepository mapGameRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

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

    /**
     * Join a player to a game. A player can only join
     * a game if the game has not started, has room for new players, the player has
     * choosen a valid monster and the user is not currently playing other game.
     * 
     * @param newPlayer
     * @param game
     */
    @Transactional
    public void joinGame(Player newPlayer, Game game) throws NewGameException {
        Monster monster = newPlayer.getMonster();
        User user = newPlayer.getUser();

        if (game.hasRoom() &&
                !game.isStarted() &&
                game.monsterAvailable(monster) &&
                !user.hasActivePlayer() &&
                (!user.hasActiveGameAsCreator() || user.isCreator(game))) {

            newPlayer.setGame(game);
            newPlayer.setUser(user);
            newPlayer.setEnergyPoints(0);
            newPlayer.setLifePoints(10);
            newPlayer.setVictoryPoints(0);
            newPlayer.setTurnsTokyo(0l);
            newPlayer.setLocation(LocationType.FUERATOKYO);
            newPlayer.setRecentlyHurt(Boolean.FALSE);

            game.getPlayers().add(newPlayer);
        } else {
            String error ="El usuario {id: '" + user.getId() + "', Username: '" + user.getUsername()
            + "'} no puede unirse a la partida {id: '" + game.getId() + "', Name: '" + game.getName()
            + "'} con el monstruo {Monster: " + monster.getName() + "}";
            if(user.hasActiveGameAsCreator() && !user.isCreator(game)){
                throw new NewGameException(error + "porque tiene una partida creada sin iniciar");
            }else{
                throw new NewGameException(error);
            }
        }
    }

    @Transactional
    public Player findPlayerById(int id) throws DataAccessException, NotFoundException {
        Optional<Player> player = playerRepository.findById(id);
        if(!player.isEmpty()){
            return player.get();
        }else{
            throw new NotFoundException("Player {id:"+id+"} no encontrado");
        }
    }

    @Transactional
    public void useRoll(Player playerActualTurn, Roll roll) {
        List<Player> playersInGameList = playerActualTurn.getGame().getPlayers();

        // Use all the cards that are used when the player does the last roll
        useCardsInRoll(playerActualTurn);

        Map<String, Integer> rollCount = countRollValues(roll.getValues());
        Map<String, Integer> cardValuesCount = countRollValues(roll.getCardExtraValues());

        Integer heal = rollCount.get("heal") + cardValuesCount.get("heal");
        Integer damage = rollCount.get("damage") + cardValuesCount.get("damage");
        Integer energys = rollCount.get("energy") + cardValuesCount.get("energy");
        Integer ones = rollCount.get("ones") + cardValuesCount.get("ones");
        Integer twos = rollCount.get("twos") + cardValuesCount.get("twos");
        Integer threes = rollCount.get("threes") + cardValuesCount.get("threes");

        // Handle heal
        if (playerActualTurn.getLocation() == LocationType.FUERATOKYO) {
            healDamage(playerActualTurn, heal);
        }

        // Entering in Tokyo
        damage = enterTokyoInRollIfEmpty(playerActualTurn, playersInGameList, damage);

        // Handle energy
        Integer sumaEnergias = playerActualTurn.getEnergyPoints() + energys;
        playerActualTurn.setEnergyPoints(sumaEnergias);

        // Handle score
        Integer totalPoints = calculatePoints(ones, twos, threes);
        playerActualTurn.setVictoryPoints(playerActualTurn.getVictoryPoints() + totalPoints);

        // Handle damage
        handleDamageRolls(playerActualTurn, playersInGameList, damage);

        // Use all the cards that are used after all the roll effects are done
        useCardsAfterRoll(playerActualTurn);

    }

    @Transactional
    public void handleDamageRolls(Player actualPlayer, List<Player> playersInGame, Integer damage) {
        Boolean actualPlayerOutOfTokyo=actualPlayer.getLocation() == LocationType.FUERATOKYO;
        for (Player player : playersInGame) {
            Boolean damagedPlayerIsActualPlayer=actualPlayer.getId().equals(player.getId());
            Boolean playerDamagedInTokyo=player.getLocation() == LocationType.CIUDADTOKYO
            || player.getLocation() == LocationType.BAHIATOKYO;

            Boolean playersInDifferentLocations=(actualPlayerOutOfTokyo && playerDamagedInTokyo) || 
                            (!actualPlayerOutOfTokyo && !playerDamagedInTokyo);
                   
            if (!damagedPlayerIsActualPlayer && playersInDifferentLocations){
                    damagePlayer(player, damage);
                if (damage >= 1 && playerDamagedInTokyo) { // If damage is done to other players
                    player.setRecentlyHurt(Boolean.TRUE);
                }
            }
            savePlayer(player);
        }
    }

    @Transactional
    public Integer enterTokyoInRollIfEmpty(Player actualPlayer, List<Player> listPlayersInGame, Integer damage) {
        Boolean bayInPlay = listPlayersInGame.stream().filter(p -> !p.isDead()).count() > 4;
        Boolean tokyoCityEmpty = !listPlayersInGame.stream()
                .anyMatch(p -> p.getLocation().equals(LocationType.CIUDADTOKYO));
        Boolean tokyoBayEmpty = !listPlayersInGame.stream()
                .anyMatch(p -> p.getLocation().equals(LocationType.BAHIATOKYO));

        if (tokyoCityEmpty && damage > 0) {
            actualPlayer.setLocation(LocationType.CIUDADTOKYO);
            actualPlayer.setVictoryPoints(actualPlayer.getVictoryPoints() + 1);
            damage--;
        } else if (bayInPlay && tokyoBayEmpty && damage > 0) {
            actualPlayer.setLocation(LocationType.BAHIATOKYO);
            actualPlayer.setVictoryPoints(actualPlayer.getVictoryPoints() + 1);
            damage--;
        }

        return damage;
    }

    @Transactional
    public void useCardsInRoll(Player player) {
        for (Card card : player.getAvailableCards()) {
            card.getCardEnum().effectInRoll(player, pService, mapGameRepository);
        }
    }

    @Transactional
    public void useCardsAfterRoll(Player player) {
        for (Card card : player.getAvailableCards()) {
            card.getCardEnum().effectAfterRoll(player, pService, mapGameRepository);
        }
    }

    // This function is used to count the different roll values of a List of
    // diceValues
    @Transactional
    public Map<String, Integer> countRollValues(List<DiceValues> values) {
        Integer heal, damage, energys, ones, twos, threes;
        heal = damage = energys = ones = twos = threes = 0;

        Map<String, Integer> rollValues = new HashMap<String, Integer>();

        for (DiceValues valorDado : values) {
            switch (valorDado) {
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

    // This function heals a player, without overpassing their max health points
    @Transactional
    public void healDamage(Player player, Integer healPoints) {
        healPoints = player.getLifePoints() + healPoints;
        Integer playerMaxHealth = player.getMaxHealth();
        if (healPoints <= playerMaxHealth) {
            player.setLifePoints(healPoints);
        } else {
            player.setLifePoints(playerMaxHealth);
        }
    }

    // This function calculates the points a player gets with the roll dices
    @Transactional
    public Integer calculatePoints(Integer ones, Integer twos, Integer threes) {
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

    // This function is called whenever any player is damaged
    @Transactional
    public void damagePlayer(Player player, Integer damage) {
        damage = useCardsInDamage(player, damage);
        Integer damagedLife = player.getLifePoints() - damage;
        Integer minHealth = 0;
        if (minHealth < damagedLife) {
            player.setLifePoints(damagedLife);
        } else {
            player.setLifePoints(minHealth);
            List<Integer> turnList = mapGameRepository.getTurnList(player.getGame().getId());
            Integer positionInTurnList = turnList.indexOf(player.getId());
            if (positionInTurnList >= 0) { // If the player is in the turnlist
                turnList.remove(player.getId());
                mapGameRepository.putTurnList(player.getGame().getId(), turnList);
            }
            player.setLocation(LocationType.FUERATOKYO);
        }
    }

    // Use all card from a player that are activated
    @Transactional
    public Integer useCardsInDamage(Player player, Integer damage) {
        for (Card card : player.getAvailableCards()) {
            damage = card.getCardEnum().effectDamage(player, pService, damage, mapGameRepository);
        }
        return damage;
    }

    @Transactional
    public void substractVictoryPointsPlayer(Player player, Integer victoryPoints) {
        Integer victoryPointsNew = player.getVictoryPoints() - victoryPoints;
        Integer minVictoryPoints = 0;
        if (minVictoryPoints < victoryPointsNew) {
            player.setVictoryPoints(victoryPointsNew);
        } else {
            player.setVictoryPoints(minVictoryPoints);
        }
    }

    @Transactional
    public void startTurn(Player player) {
        User user = player.getUser();
        Integer pointsObtainedInTokyo = 2;
        if (player.getLocation().equals(LocationType.CIUDADTOKYO)
                || player.getLocation().equals(LocationType.BAHIATOKYO)) {

            player.setVictoryPoints(player.getVictoryPoints() + pointsObtainedInTokyo);
            player.setTurnsTokyo(player.getTurnsTokyo() + 1l);
            savePlayer(player);

            if(player.getTurnsTokyo()>user.getMaxTurnsTokyo()){
                user.setMaxTurnsTokyo(player.getTurnsTokyo());
                userService.saveUser(user);
            }
        }

    }

    @Transactional
    public Player actualPlayer(Game game) {
        User user = userService.authenticatedUser();
        Optional<Player> player = game.getPlayers().stream().filter(p -> p.getUser().getId().equals(user.getId())).findAny();
        if(player.isPresent()){
            return player.get();
        }else{
            return null;
        }
    }

    @Transactional
    public void surrender(Player player) {
        User user = userService.authenticatedUser();
        if (player.getUser().getId().equals(user.getId())) {

            List<PlayerCard> playerCards = player.getPlayerCard();
            playerCards.forEach(card -> card.setDiscarded(Boolean.TRUE));
            player.setPlayerCard(playerCards);
            damagePlayer(player, 99);
            gameService.endGame(player.getGame());
            savePlayer(player);
        }
    }

    /**
     * @return True if the player has been hurt (the property recentlyhurt of the
     *         player equals true)
     */
    @Transactional
    public Boolean isRecentlyHurt(Integer gameId) {
        User user = userService.authenticatedUser();
        Player player = gameService.playerInGameByUser(user, gameId);
        Boolean result = Boolean.FALSE;
        if (player.getRecentlyHurt() == Boolean.TRUE) {
            result = Boolean.TRUE;
        }
        return result;
    }

    /**
     * @return True if the player is in TokyoCity or TokyoBay.
     */
    @Transactional
    public Boolean isInTokyo(Integer gameId) {
        User user = userService.authenticatedUser();
        Player player = gameService.playerInGameByUser(user, gameId);
        Boolean result = Boolean.FALSE;
        if (player.getLocation() == LocationType.BAHIATOKYO || player.getLocation() == LocationType.CIUDADTOKYO) {
            result = Boolean.TRUE;
        }
        return result;
    }

    /**
     * Checks is the number of player is less than 5 and disables Tokyo Bay. 
     * @param gameId
     */
    @Transactional
    public void checkplayers(Game game) {
        List<Player> players = game.playersAlive();
        Integer minAmmountPlayersTokyoBay = 5;
        if(players.size()<minAmmountPlayersTokyoBay){
            for(Player player : players){
                if(player.getLocation()==LocationType.BAHIATOKYO){
                    player.setLocation(LocationType.FUERATOKYO);
                }
            }
        }
    }

}