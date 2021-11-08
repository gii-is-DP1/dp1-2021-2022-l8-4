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
public class UserServiceKoT {
    
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
	public void saveUser(UserKoT user) {
		userRepository.save(user);
	}
	
    @Transactional
    public Optional<UserKoT> findUser(String username){
    	return userRepository.findById(username);
    }
}
