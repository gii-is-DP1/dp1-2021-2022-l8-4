package org.springframework.samples.petclinic.player;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @author Ricardo Nadal Garcia
 * @author Noelia López Durán
 */

@Service
public class PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;

    @Transactional
    public Iterable<Player> findAll(){
        Iterable<Player> res=playerRepository.findAll();
        return res;
    }

    @Transactional
    public int playerCount(){
        return (int) playerRepository.count();
    } 

    @Transactional(rollbackFor = DuplicatedMonsterNameException.class)
    public void savePlayer(Player player) throws DuplicatedMonsterNameException {
        Player otherPlayer=getPlayerwithIdDifferent(player.getMonsterName().toString(), player.getId());
            if (StringUtils.hasLength(player.getMonsterName().toString()) &&  (otherPlayer!= null && otherPlayer.getId()!=player.getId())) {            	
            	throw new DuplicatedMonsterNameException();
            }else
                playerRepository.save(player);   
        playerRepository.save(player);
    }
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
	public Player findPlayerById(int id) throws DataAccessException {
		return playerRepository.findById(id).get();
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
    public void useRoll(int gameId, Integer playerIdActualTurn, Roll roll) throws DuplicatedMonsterNameException {
        List<Player> listaJugadoresEnPartida=findPlayerByGame(gameId);

        
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

        for(Player player:listaJugadoresEnPartida) {
            Integer playerMaxHealth=10; //Por ahora lo dejo asi, la idea es que sea 10 default o 12 si tiene la carta (max health Atributo de player?)
            Integer playerMinHealth=0; 
            if(playerIdActualTurn == player.getId()){
                //CURACION
                
                Integer sumaVida=player.getLifePoints() + heal;
                if(sumaVida <= playerMaxHealth) { //Hay que poner la condicion de si esta en tokyo no se cura
                    player.setLifePoints(sumaVida);
                } else{
                    player.setLifePoints(playerMaxHealth);
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
                //Daño a los otros jugadores
                Integer sumaVidaQuitada=player.getLifePoints() - damage;
                if(playerMinHealth < sumaVidaQuitada) {
                    player.setLifePoints(sumaVidaQuitada);
                } else {
                    player.setLifePoints(0);
                }
            }
            savePlayer(player);
        }
    }
    
   
}
