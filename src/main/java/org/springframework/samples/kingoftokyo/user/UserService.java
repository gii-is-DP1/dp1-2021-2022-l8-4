package org.springframework.samples.kingoftokyo.user;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.samples.kingoftokyo.player.Player;

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
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public int userCount() {
		return (int) userRepository.count();
	}

	@Transactional
	public void saveUser(User user) {
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		Authorities authority = new Authorities();
		authority.setUser(user);
		authority.setAuthority("user");
		authoritiesRepository.save(authority);
	}

	/**
	 * Find user given id
	 * 
	 * @param id
	 * @return User
	 */

	@Transactional
	public User findUserById(int id) throws DataAccessException {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()){
			return user.get();
		}else{
			throw new NotFoundException("User {id:"+id+"} no encontrado");
		}
	}

	/**
	 * Find authenticated user given username
	 * 
	 * @param currentUserUsername
	 * @return UserId
	 */

	@Transactional
	public Integer getCurrentUserId(String currentUserUsername) {
		User currentUser = userRepository.findCurrentUser(currentUserUsername);
		return currentUser.getId();
	}

	/**
	 * Find user given username
	 * 
	 * @param username
	 * @return User
	 */

	@Transactional
	public User findUserByUsername(String username) {
		int userId = getCurrentUserId(username);
		return findUserById(userId);
	}

	@Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	/**
	 * @return Autheticated User if logged in or null if no one is logged in
	 */
	@Transactional
	public User authenticatedUser() {
		User currentUser = null;
		String currentUsername = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication!=null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication
					.getPrincipal();
			currentUsername = user.getUsername();
			currentUser = findUserByUsername(currentUsername);
			
		}
		return currentUser;
	}

	/**
	 * @param pageNumber
	 * @return Obtains a page of 5 users from userRepository
	 */
	@Transactional
	public Page<User> getPageOfUsers(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by(Order.asc("username")));
		return userRepository.findAllUsersByPage(pageable);
	}

	/*
	 * Check if the authenticated user is playing as the specific player
	 * 
	 * @return true if the logged user is playing as the player
	 */
	@Transactional
	public Boolean isAuthUserPlayingAsPlayer(Player player) {
		User user = authenticatedUser();
		if (user instanceof User) {
			return user.getPlayers().stream().map(p -> p.getId()).filter(id -> player.getId().equals(id) ).findAny()
					.isPresent();
		} else {
			return false;
		}
	}

	@Transactional
	public Boolean isAdmin(int userId){
		Optional<Authorities> auth = authoritiesRepository.findById(userId);
		if(auth.isPresent()){
			int res = auth.get().authority.compareTo("admin");
			return res==0 ? true : false;
		}else{
			return false;
		}
	}

}
