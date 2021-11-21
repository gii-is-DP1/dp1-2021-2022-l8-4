package org.springframework.samples.petclinic.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Sara Cruz
 * @author Rosa Molina
 * @author Carlos Varela Soult
 * @author Jose Maria Delgado Sanchez
 */

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	public UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}

	@Transactional
	public Iterable<User> findAll() {
		Iterable<User> res = userRepository.findAll();
		return res;
	}

	@Transactional
	public int userCount() {
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

	@Transactional
	public User findUserByUsername(String username){
		int userId = getCurrentUserId(username);
		return findUserById(userId).get();
	}


	/**
	 * @return Autheticated User if logged in or null if no one is logged in
	 */
	@Transactional
	public String authenticatedUser() {
		String currentUser = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
				org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
				currentUser = user.getUsername();
			}
		}
		return currentUser;
	}

}
