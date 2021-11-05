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
public class UserService {
    
   private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
 
	@Transactional
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	public Optional<User> findUser(String username) throws DataAccessException {
		return userRepository.findById(username);
	}

	@Transactional
	public Iterable<User> findAll(){
        Iterable<User> res = userRepository.findAll();
        return res;
    }

	@Transactional
	public int userCount(){
		return (int) userRepository.count();
	}
}
