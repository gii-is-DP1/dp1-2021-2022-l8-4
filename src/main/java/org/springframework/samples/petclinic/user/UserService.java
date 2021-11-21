package org.springframework.samples.petclinic.user;

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
public class UserService{
    
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private AuthoritiesRepository authoritiesRepository;

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
		user.setEnabled(true);
		userRepository.save(user);
		Authorities authority = new Authorities();
		authority.setUser(user);
		authority.setAuthority("user");
		authoritiesRepository.save(authority);
	}

	@Transactional
	public Optional<User> findUserById(int id) throws DataAccessException {
		return userRepository.findById(id);
	}
	
	@Transactional
	public Integer getCurrentUserId(String currentUserUsername) {
		User currentUser = userRepository.findCurrentUser(currentUserUsername);
		Integer currentUserId = currentUser.getId();
		return currentUserId;
	}
	
	

}
