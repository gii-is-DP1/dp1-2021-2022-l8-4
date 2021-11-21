package org.springframework.samples.petclinic.userKoT;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


/**
* @author Sara Cruz
* @author Rosa Molina
* @author Carlos Varela Soult
*/

@Service
public class UserServiceKoT{
    
	@Autowired 
	private UserRepositoryKoT userRepository;
	@Autowired
	private AuthoritiesRepositoryKoT authoritiesRepository;

	@Transactional
	public Iterable<UserKoT> findAll(){
        Iterable<UserKoT> res = userRepository.findAll();
        return res;
    }

	@Transactional
	public int userCount(){
		return (int) userRepository.count();
	}
 
	@Transactional
	public void saveUser(UserKoT user) {
		user.setEnabled(true);
		userRepository.save(user);
		AuthoritiesKoT authority = new AuthoritiesKoT();
		authority.setUserkot(user);
		authority.setAuthority("user");
		authoritiesRepository.save(authority);
	}

	@Transactional
	public Optional<UserKoT> findUserkotById(int id) throws DataAccessException {
		return userRepository.findById(id);
	}
	
	@Transactional
	public Integer getCurrentUserId(String currentUserUsername) {
		UserKoT currentUserKoT = userRepository.findCurrentUser(currentUserUsername);
		Integer currentUserId = currentUserKoT.getId();
		return currentUserId;
	}
}
