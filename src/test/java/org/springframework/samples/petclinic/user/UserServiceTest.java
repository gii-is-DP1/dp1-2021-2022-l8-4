package org.springframework.samples.petclinic.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
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
    public void testUpdateCardName() throws Exception {
        Optional<User> user = userService.findUserById(1);

        String newName = "Admin";
        user.get().setUsername(newName);
        this.userService.saveUser(user.get());

        user = this.userService.findUserById(1);
        assertThat(user.get().getUsername()).isEqualTo(newName);
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
        assertEquals(userService.userCount(), 4);
        //Hacer algo para que en vez de coger 4
        //se haga userService.findAll().count o algo del estilo
        //asi siempre funciona y probamos los dos
	}



}
