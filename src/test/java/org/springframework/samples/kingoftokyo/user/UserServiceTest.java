package org.springframework.samples.kingoftokyo.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.game.GameService;
import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.Achievement;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

/**
 * @author Carlos Varela Soult
 * @author Sara Cruz Duárez
 * @author Rosa Molina Arregui
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({SecurityConfiguration.class, MapGameRepository.class})

class UserServiceTest {
    
    @Autowired
    private UserService userService;
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private AchievementService achievementService;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    private Integer numeroUsuarios = 25;

    @Test
    void testGetCurrentUserId() {
        Integer currentUserId = userService.getCurrentUserId("user2");
        assertThat(currentUserId).isEqualTo(2);
    }

    @Test
    void testSaveUserIntoDatabaseAndGenerateId() {
        User user = new User();
        user.setUsername("rosmolarr");
        user.setEmail("rosmolarr@alum.us.es");
        user.setPassword("rosmolarr");
        userService.saveUser(user,true);
        assertThat(user.getId()).isNotNull();
        Integer id = userService.findUserByUsername("rosmolarr").getId();
        assertEquals(user.getId(), id);
    }

    
    @Test
    void testFindUserById() {
        User user = userService.findUserById(1);
        assertEquals("user1@email.com", user.getEmail());
        assertEquals("user1", user.getUsername());
        assertEquals(true, user.isEnabled());
        
    }

    @Test
    void testGetAndSetAchievements(){
        User user = userService.findUserById(1);
        Set<Achievement> setAchievement = new HashSet<>();
        Achievement achievement = achievementService.findAchievementById(1).get();
        setAchievement.add(achievement);
        user.setAchievements(setAchievement);
        assertEquals(setAchievement, user.getAchievements());
    }

    @Test
    void testGetAndSetGames() throws DataAccessException, NotFoundException{
        User user = userService.findUserById(1);
        Set<Game> setGame = new HashSet<>();
        Game game = this.gameService.findGameById(1);
        setGame.add(game);
        assertEquals(setGame, user.getGames());
    }

    @Test
    void testSetPlayers(){
        User user = new User();
        user.setUsername("userTestSetPlayer");
        Player player = new Player();
        player.setUser(user);
        Set<Player> setPlayer = new HashSet<>();
        setPlayer.add(player);
        user.setPlayers(setPlayer);
        assertEquals(setPlayer, user.getPlayers());
    }

    @Test
    void testSetAuthorities(){
        User user = new User();
        user.setUsername("userTestSetPlayer2");
        Authorities auth = new Authorities();
        auth.setAuthority("test");
        Set<Authorities> setAuth = new HashSet<>();
        user.setAuthorities(setAuth);
        assertEquals(setAuth, user.getAuthorities());
    }

    @Test
	void testUpdateUserName() throws Exception {
		User user = userService.findUserById(1);

		String newName = "Admin";
		user.setUsername(newName);
        this.userService.saveUser(user,true);

		user = this.userService.findUserById(1);
		assertThat(user.getUsername()).isEqualTo(newName);
	}

    @Test
	void testCountUsers() throws Exception {
        Integer contador1 = userService.userCount();
        Integer numero = numeroUsuarios;
    
        assertEquals(contador1, numero);
        User user = new User();
        user.setUsername("user4");
        user.setEmail("user4@correo.com");
        user.setPassword("user4");
        userService.saveUser(user,true);

        Integer contador2NewUser = userService.userCount();
        numero += 1;
        assertEquals(contador2NewUser, numero);

	}

    @Test
	void testFindAllUsers() throws Exception {
        List<User> listcont= new ArrayList<>();
        userService.findAll().forEach(listcont::add);
        Integer numero = numeroUsuarios;
        Integer contadorFind = listcont.size();
        assertEquals(contadorFind, numero);

        User user = new User();
        user.setUsername("user4");
        user.setEmail("user4@correo.com");
        user.setPassword("user4");
        userService.saveUser(user,true);

        List<User> listcont2= new ArrayList<>();
        userService.findAll().forEach(listcont2::add);
        Integer contadorFind2 = listcont2.size();
        numero +=1 ;
        assertEquals(contadorFind2, numero);
	}

    @Test
    void testGetPageOfUsers() {
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
    void testDeleteUser() {
        List<User> currentListUsers = new ArrayList<>();
        List<User> newListUsers = new ArrayList<>();
        userService.findAll().forEach(currentListUsers::add);
        Integer currentCount = currentListUsers.size();

        userService.deleteUser(userService.findUserById(1));
        userService.findAll().forEach(newListUsers::add);
        Integer newCount = newListUsers.size();
        assertThat(newCount).isEqualTo(currentCount-1);
    }

    @Test
    void testIsAdmin() {
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
        User userWhoIsAdmin = userService.findUserById(5);
        assertThat(listAdmins.contains(userWhoIsAdmin)).isTrue();
    }

    @Test
    void testHasActivePlayer() {
        User userPlayerActive = userService.findUserById(10);
        assertTrue(userPlayerActive.hasActivePlayer());
        User userPlayerActiveButDied = userService.findUserById(1);
        assertFalse(userPlayerActiveButDied.hasActivePlayer());
    }

    @Test
    void testHasActiveGameAsCreator() throws DataAccessException, NotFoundException {
        User userGameActive = userService.findUserById(23);
        assertTrue(userGameActive.hasActiveGameAsCreator());
    }

    @Test
    void testAuthenticatedUserIsNull() {
        assertNull(userService.authenticatedUser());
    }

    @Test
    void testUserIsAdmin() {
        assertTrue(userService.isAdmin(1));
    }

    @Test
    void testUserIsNotAdmin() {
        assertFalse(userService.isAdmin(14));
    }

    @Test
    void testUserIsNotAuth() {
        User user = new User();
        user.setUsername("usuariotest");
        user.setEmail("usuariotest@email.com");
        user.setPassword("usuariotest");
        userService.saveUser(user, Boolean.FALSE);
        assertFalse(userService.isAdmin(user.getId()));
    }

    @Test
    void testUserIsNotLoginPlaying() throws DataAccessException, NotFoundException {
        assertFalse(userService.isAuthUserPlayingAsPlayer(playerService.findPlayerById(1)));
    }

    @Disabled
    @Test
    void shouldChangePassword() throws Exception{
        User user = new User();
        user.setUsername("usuariotest");
        user.setEmail("usuariotest@email.com");
        user.setPassword("usuariotest");
        userService.saveUser(user, Boolean.FALSE);
        User user1 = userService.findUserById(1);

        userService.passwordCheckEdit("admin", "newPassword", user1);



    }
}
