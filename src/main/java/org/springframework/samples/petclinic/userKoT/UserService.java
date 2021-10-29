package org.springframework.samples.petclinic.userKoT;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.user.UserRepository;
import org.springframework.stereotype.Service;


/**
* @author Sara Cruz
* @author Rosa Molina
*/

@Service
public class UserService {
    
   private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
 
	@Transactional
	public void saveUser(User user) throws DataAccessException {
		userRepository.save(user);
	}
	
	public Optional<User> findUser(String username) {
		return userRepository.findById(username);
	}
 
}
