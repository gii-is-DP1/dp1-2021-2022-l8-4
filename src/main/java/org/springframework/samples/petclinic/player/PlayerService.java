package org.springframework.samples.petclinic.player;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
}
