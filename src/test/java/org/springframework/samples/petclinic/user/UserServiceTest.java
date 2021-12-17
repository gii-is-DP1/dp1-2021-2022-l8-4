package org.springframework.samples.petclinic.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
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
        Integer contador1 = userService.userCount();
        Integer numero = 9;
    
        assertEquals(contador1, numero);
        User user = new User();
        user.setUsername("user4");
        user.setEmail("user4@correo.com");
        user.setPassword("user4");
        userService.saveUser(user);

        Integer contador2NewUser = userService.userCount();
        numero = 10;
        assertEquals(contador2NewUser, numero);

	}

    @Test
	public void testFindAllUsers() throws Exception {
        List<User> listcont= new ArrayList<>();
        userService.findAll().forEach(listcont::add);
        Integer numero = 9;
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
        numero = 10;
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

        userService.findAll().forEach(currentListUsers::add); // size = 9
        Integer currentCount = currentListUsers.size(); // size=9

        userService.deleteUser(userService.findUserById(1).get()); // size=8
        
        for (User user:userService.findAll()) {
            newListUsers.add(user);
        }

        Integer newCount = newListUsers.size(); // size=8
        assertThat(currentCount).isEqualTo(newCount-1);
    }

}
