package org.springframework.samples.petclinic.player;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.stereotype.Service;

/**
 * @author Ricardo Nadal Garcia
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

    @Transactional
	public Player findPlayerById(int id) throws DataAccessException {
		return playerRepository.findById(id).get();
	}

    @Transactional
    public List<DiceValues> roll(){
        return Roll.rollDice();
    }
}
