package org.springframework.samples.petclinic.userKoT;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


/**
* @author Sara Cruz
* @author Rosa Molina
*/

@Service
public class UserServiceKoT{
    
	@Autowired 
	private UserRepositoryKoT userRepository;

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
	public Optional<UserKoT> findUserkotById(int id) throws DataAccessException {
		return userRepository.findById(id);
	}

	@Autowired
	public AuthoritiesServiceKoT authoritiesService;

	@Transactional
	public void saveUser(UserKoT user) {
		user.setEnabled(true);
		userRepository.save(user);
		authoritiesService.saveAuthorities(user.getId(), "user");
	}

}
