package org.springframework.samples.petclinic.player;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
}