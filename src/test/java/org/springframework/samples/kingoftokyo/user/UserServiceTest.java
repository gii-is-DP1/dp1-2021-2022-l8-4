package org.springframework.samples.kingoftokyo.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.samples.kingoftokyo.user.Authorities;
import org.springframework.samples.kingoftokyo.user.AuthoritiesService;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Carlos Varela Soult
 * @author Sara Cruz Duárez
 * @author Rosa Molina Arregui
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    @Autowired
    private AuthoritiesService authoritiesService;

    private Integer numeroUsuarios = 25;

    @Test
    public void testGetCurrentUserId() {
        Integer currentUserId = userService.getCurrentUserId("user2");
        assertThat(currentUserId).isEqualTo(2);
    }

    @Test
    public void testSaveUserIntoDatabaseAndGenerateId() {
        User user = new User();
        user.setUsername("rosmolarr");
        user.setEmail("rosmolarr@alum.us.es");
        user.setPassword("rosmolarr");
        userService.saveUser(user);
        assertThat(user.getId()).isNotNull();
        Integer id = userService.findUserByUsername("rosmolarr").getId();
        assertEquals(user.getId(), id);
    }

    
    @Test
    public void testFindUserById() {
        Optional<User> user = userService.findUserById(1);
        assertEquals(user.get().getEmail(), "user1@email.com");
        assertEquals(user.get().getPassword(), "u53r1");
        assertEquals(user.get().getUsername(), "user1");
        assertEquals(user.get().isEnabled(), true);
        
    }

    @Test
	public void testUpdateUserName() throws Exception {
		Optional<User> user = userService.findUserById(1);

		String newName = "Admin";
		user.get().setUsername(newName);
        this.userService.saveUser(user.get());

		user = this.userService.findUserById(1);
		assertThat(user.get().getUsername()).isEqualTo(newName);
	}

    @Test
	public void testCountUsers() throws Exception {
        Integer contador1 = userService.userCount();
        Integer numero = numeroUsuarios;
    
        assertEquals(contador1, numero);
        User user = new User();
        user.setUsername("user4");
        user.setEmail("user4@correo.com");
        user.setPassword("user4");
        userService.saveUser(user);

        Integer contador2NewUser = userService.userCount();
        numero += 1;
        assertEquals(contador2NewUser, numero);

	}

    @Test
	public void testFindAllUsers() throws Exception {
        List<User> listcont= new ArrayList<>();
        userService.findAll().forEach(listcont::add);
        Integer numero = numeroUsuarios;
        Integer contadorFind = listcont.size();
        assertEquals(contadorFind, numero);

        User user = new User();
        user.setUsername("user4");
        user.setEmail("user4@correo.com");
        user.setPassword("user4");
        userService.saveUser(user);

        List<User> listcont2= new ArrayList<>();
        userService.findAll().forEach(listcont2::add);
        Integer contadorFind2 = listcont2.size();
        numero +=1 ;
        assertEquals(contadorFind2, numero);
	}

    @Test
    public void testGetPageOfUsers() {
        int countUsers=0;
        int pageId=0;
        Page<User> page = userService.getPageOfUsers(pageId);
        while(pageId<page.getTotalPages()) {
            page = userService.getPageOfUsers(pageId);
            countUsers = (int) page.getContent().stream().count();
            assertThat(countUsers).isLessThanOrEqualTo(page.getSize());
            pageId = pageId+1;
        }
    }

    @Test
    public void testDeleteUser() {
        List<User> currentListUsers = new ArrayList<>();
        List<User> newListUsers = new ArrayList<>();
        userService.findAll().forEach(currentListUsers::add);
        Integer currentCount = currentListUsers.size();

        userService.deleteUser(userService.findUserById(1).get());
        userService.findAll().forEach(newListUsers::add);
        Integer newCount = newListUsers.size();
        assertThat(newCount).isEqualTo(currentCount-1);
    }

    @Test
    public void testIsAdmin() {
        List<User> listAdmins = new ArrayList<>();
        List<User> allUsers = new ArrayList<>();
        List<Authorities> listAuthorities = new ArrayList<>();
        authoritiesService.findAll().forEach(listAuthorities::add);
        // Obtains all authorities who are admin
        listAuthorities.stream()
                       .filter(auth->auth.getAuthority().equals("admin"))
                       .collect(Collectors.toList());

        // Obtains all users and iterates over them and their authorities to save
        // those users who are admin
        userService.findAll().forEach(allUsers::add);
        for (Authorities auth:listAuthorities) {
            for (User user:allUsers) {
                Set<Authorities> authority = user.getAuthorities();
                if(authority.toString().contains(auth.getAuthority())) {
                    listAdmins.add(user);
                }
            }
        }
       
        // Checks if some user who is admin is in all admins list
        User userWhoIsAdmin = userService.findUserById(5).get();
        assertThat(listAdmins.contains(userWhoIsAdmin));
    }

}
