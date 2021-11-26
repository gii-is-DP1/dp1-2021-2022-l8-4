package org.springframework.samples.petclinic.player;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
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

    @Transactional
    public Iterable<Player> findAll(){
        Iterable<Player> res=playerRepository.findAll();
        return res;
    }

    @Transactional
    public int playerCount(){
        return (int) playerRepository.count();
    } 
/*
    @Transactional(rollbackFor = DuplicatedMonsterNameException.class)
    public void savePlayer(Player player) throws DuplicatedMonsterNameException {
        Player otherPlayer=getPlayerwithIdDifferent(player.getMonsterName().toString(), player.getId());
            if (StringUtils.hasLength(player.getMonsterName().toString()) &&  (otherPlayer!= null && otherPlayer.getId()!=player.getId())) {            	
            	throw new DuplicatedMonsterNameException();
            }else
                playerRepository.save(player);   
        playerRepository.save(player);
    }
    
*/

    public Player getPlayerwithIdDifferent(String monsterName,Integer id) {
		monsterName = monsterName.toLowerCase();
		for (Player player : playerRepository.findAll()) {
			String compName = player.getMonsterName().toString();
			compName = compName.toLowerCase();
			if (compName.equals(monsterName) && player.getId()!=id) {
				return player;
			}
		}
		return null;
	}

    @Transactional
    public void savePlayer(Player player){
        playerRepository.save(player);
    }

    @Transactional
    public void joinGame(User user, Player player, Game game){
        MonsterName monsterName = player.getMonsterName();
        if(game.hasRoom()
            && !game.isStarted()
            && game.monsterAvailable(monsterName)
            && !user.hasActivePlayer()){
            
                player.setGame(game);
                player.setUser(user);
                player.setEnergyPoints(0);
                player.setLifePoints(10);
                player.setVictoryPoints(0);
                player.setLocation(LocationType.fueraTokyo);

                savePlayer(player);
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
    public List<PlayerStatus> findPlayerStatus(int playerId){
        return playerStatusRepository.findByPlayerId(playerId);
        
    }
    @Transactional(readOnly = true)
	public List<StatusType> findStatusTypes() throws DataAccessException {
        List<StatusType> ct = new ArrayList<StatusType>();
        ct.add(StatusType.Veneno);ct.add(StatusType.Reductor);
		return ct;
	}

    @Transactional
    public List<Player> findPlayerByGame(Integer gameId) {
        Iterable<Player> allPlayers=findAll();
        List<Player> listaJugadores=new ArrayList<Player>();
        for(Player player:allPlayers) {
            if(player.getGame().getId()==gameId) {
                listaJugadores.add(player);
            }
        }
        return  listaJugadores;
    }

    

    @Transactional
    public void useRoll(int gameId, Integer playerIdActualTurn, Roll roll) {
        List<Player> listaJugadoresEnPartida=findPlayerByGame(gameId);
        Player playerActualTurn = findPlayerById(playerIdActualTurn);
        Boolean tokyoCityEmpty=Boolean.FALSE;
        Boolean tokyoBayEmpty=Boolean.FALSE;
        
        Integer heal=0;
        Integer damage=0;
        Integer energys=0;
        Integer ones=0;
        Integer twos=0;
        Integer threes=0;


        for(DiceValues valorDado:roll.getValues()){
            switch(valorDado){ //Lo estoy dejando de esta manera tan extensa por si luego hay que tener en cuenta las cartas para cada tipo de dado
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
        //Si tokyo tiene espacio
        Boolean bayInPlay=listaJugadoresEnPartida.stream().filter(p-> !p.isDead()).count() > 4;
        tokyoCityEmpty= !listaJugadoresEnPartida.stream().anyMatch(p -> p.getLocation().equals(LocationType.ciudadTokyo));
        tokyoBayEmpty=  !listaJugadoresEnPartida.stream().anyMatch(p -> p.getLocation().equals(LocationType.bahiaTokyo));

        if(tokyoCityEmpty && damage > 0) {
            playerActualTurn.setLocation(LocationType.ciudadTokyo);
            playerActualTurn.setVictoryPoints(playerActualTurn.getVictoryPoints() + 1);
            damage--;
        } else if(bayInPlay && tokyoBayEmpty && damage > 0) {
            playerActualTurn.setLocation(LocationType.bahiaTokyo);
            playerActualTurn.setVictoryPoints(playerActualTurn.getVictoryPoints() + 1);
            damage--;
        }
        //Los efectos de los dados
        for(Player player:listaJugadoresEnPartida) {
            Integer playerMaxHealth=10; //Por ahora lo dejo asi, la idea es que sea 10 default o 12 si tiene la carta (max health Atributo de player?)
            Integer playerMinHealth=0; 
            

            if(playerIdActualTurn == player.getId()){
                //CURACION
                if(player.getLocation()==LocationType.fueraTokyo){
                    Integer sumaVida=player.getLifePoints() + heal;
                    if(sumaVida <= playerMaxHealth) { 
                        player.setLifePoints(sumaVida);
                    } else{
                        player.setLifePoints(playerMaxHealth);
                    }
                }
                //ENERGIAS
                Integer sumaEnergias=player.getEnergyPoints()+ energys;
                player.setEnergyPoints(sumaEnergias);

                //PUNTUACION 
                Integer sumaTotal=0;
                Integer sumaOnes=(ones - 2);
                if(sumaOnes > 0) { sumaTotal+=sumaOnes;}
                Integer sumaTwos=(twos - 1) ;
                if(twos-2 > 0) { sumaTotal+=sumaTwos;}
                Integer sumaThrees=threes;
                if(threes-2 > 0) { sumaTotal+=sumaThrees;}
                player.setVictoryPoints(player.getVictoryPoints()+sumaTotal);

            } else {
                //Daño a los otros jugadores estando fuera de tokyo
                if(playerActualTurn.getLocation()==LocationType.fueraTokyo){
                    if(player.getLocation()==LocationType.ciudadTokyo){
                        restarVida(player, damage, playerMinHealth);
                    }else if(player.getLocation()==LocationType.bahiaTokyo){
                        restarVida(player, damage, playerMinHealth);
                    }
                //Daño a otros jugadores estando en Tokyo (ciudad o bahía)
                }else if(playerActualTurn.getLocation()==LocationType.bahiaTokyo 
                    || playerActualTurn.getLocation()== LocationType.ciudadTokyo){
                        if(player.getLocation()==LocationType.fueraTokyo){
                            restarVida(player, damage, playerMinHealth);
                        }
                }
            }
            savePlayer(player);
        }
    }
    
    @Transactional
    public void restarVida(Player player, Integer damage, Integer playerMinHealth){
        Integer sumaVidaQuitada=player.getLifePoints() - damage;
            if(playerMinHealth < sumaVidaQuitada) {
                player.setLifePoints(sumaVidaQuitada);
            } else {
                player.setLifePoints(0);
                player.setLocation(LocationType.fueraTokyo);
                }
    }

    @Transactional
    public void startTurn(Integer playerId){
        Player player=findPlayerById(playerId);
        if(player.getLocation().equals(LocationType.ciudadTokyo) || player.getLocation().equals(LocationType.bahiaTokyo)) {
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            savePlayer(player);
        }
    }

    @Transactional
    public Player actualPlayer(Integer gameId) {
        Game game=gameService.findGameById(gameId);
        User user=userService.authenticatedUser();
        return game.getPlayers()
                    .stream()
                    .filter(p -> p.getUser().getId().equals(user.getId()))
                    .findAny()
                    .get();
    }

    @Transactional
    public void surrender(Integer playerId) {
        Player player=findPlayerById(playerId);       
        User user=userService.authenticatedUser();
        if(player.getUser().getId() == user.getId()) {
            player.surrender();
            savePlayer(player);
        }
    }



    

}