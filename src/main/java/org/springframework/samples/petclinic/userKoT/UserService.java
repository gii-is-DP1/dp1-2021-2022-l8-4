package org.springframework.samples.petclinic.userKoT;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
* @author Sara Cruz
* @author Rosa Molina
*/

@Service
public class UserService {
    
	@Autowired UserRepository userRepository;

	@Transactional
	public Iterable<User> findAll(){
        Iterable<User> res = userRepository.findAll();
        return res;
    }

	@Transactional
	public int userCount(){
		return (int) userRepository.count();
	}
 
	@Transactional
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
    @Transactional
    public Optional<User> findUser(String username){
    	return userRepository.findById(username);
    }
}
