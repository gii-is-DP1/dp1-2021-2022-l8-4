package org.springframework.samples.kingoftokyo.game;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.configuration.CurrentUserController;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.game.exceptions.DeleteGameException;
import org.springframework.samples.kingoftokyo.game.exceptions.NewGameException;
import org.springframework.samples.kingoftokyo.player.LocationType;
import org.springframework.samples.kingoftokyo.player.Monster;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ricardo Nadal Garcia
 */

@WebMvcTest(value = LobbyController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
        
public class LobbyControllerTest {
    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private CurrentUserController currentUserController;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testLobbiesList() throws Exception {
        mockMvc.perform(get("/games/lobbies")).andExpect(status().isOk())
                .andExpect(view().name("games/lobbiesList"))
                .andExpect(model().attributeExists("lobbies"));
    }


    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testNewLobby() throws Exception {
        mockMvc.perform(get("/games/new")).andExpect(status().isOk())
                .andExpect(view().name("games/newGame"))
                .andExpect(model().attributeExists("newGame"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testPostNewLobby() throws Exception {
        mockMvc.perform(post("/games/new")
                            .with(csrf())
                            .param("name","TEST")
                            .param("maxNumberOfPlayers","3")
                            .param("turn","0")
                            .param("id","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/" + 1 + "/lobby"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testPostNewLobbyHasErrors() throws Exception {
        String nameTooShort="a";
        mockMvc.perform(post("/games/new")
                            .with(csrf())
                            .param("name",nameTooShort)
                            .param("maxNumberOfPlayers","3")
                            .param("turn","0")
                            .param("id","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/newGame"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testPostNewLobbyThrowException() throws Exception {
        doThrow(new NewGameException("")).when(gameService).createNewGame(any(Game.class));
        mockMvc.perform(post("/games/new")
                            .with(csrf())
                            .param("name","TEST")
                            .param("maxNumberOfPlayers","3")
                            .param("turn","0")
                            .param("id","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/lobbies"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testJoinNewLobby() throws Exception {
        Game game=new Game();
        Integer gameId=1;

        List<Player> players=new ArrayList<Player>();
        Player player=new Player();
        player.setMonster(Monster.king);
        players.add(player);
        game.setPlayers(players);
        
        Integer gameNotStartedTurn=0;
        game.setTurn(gameNotStartedTurn);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);

        mockMvc.perform(get("/games/"+gameId+"/lobby")).andExpect(status().isOk())
                .andExpect(view().name("games/lobby"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attributeExists("availableMonsters"))
                .andExpect(model().attributeExists("players"))
                .andExpect(model().attributeExists("newPlayer"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testJoinStartedLobby() throws Exception {
        Game game=new Game(); 
        Integer gameStartedTurn=1;      
        game.setTurn(gameStartedTurn);
        Integer gameId=1;
        game.setId(gameId);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);

        mockMvc.perform(get("/games/1/lobby")).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/"+gameId+"/playing"));
    }


    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testPostLobby() throws Exception {
        Player player=new Player();
        player.setEnergyPoints(0);
        player.setLocation(LocationType.fueraTokyo);
        player.setLifePoints(10);
        player.setMonster(Monster.alien);
        player.setVictoryPoints(0);
        
        Integer gameId=1;
        
        mockMvc.perform(post("/games/"+gameId+"/lobby")
                            .with(csrf())
                            .param("energyPoints","0")
                            .param("lifePoints","0")
                            .param("location","fueraTokyo")
                            .param("victoryPoints","0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/"+gameId+"/lobby"));
    }


    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testDeleteLobbyWithCorrectUser() throws Exception {
        Game game=new Game(); 
        Integer gameId=1;
        game.setId(gameId);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);

        User user=new User();
        Set<Game> games=new HashSet<Game>();
        games.add(game);
        user.setGames(games);
        Mockito.when(userService.authenticatedUser()).thenReturn(user);

        mockMvc.perform(get("/games/"+gameId+"/lobby/delete")).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/lobbies"));
    }

    

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testDeleteLobbyThrowingException() throws Exception {
        Game game=new Game(); 
        Integer gameId=1;
        game.setId(gameId);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);

        User user=new User();
        Set<Game> games=new HashSet<Game>();
        games.add(game);
        user.setGames(games);

        Mockito.when(userService.authenticatedUser()).thenReturn(user);

        doThrow(new DeleteGameException()).when(gameService).deleteGame(game);

        mockMvc.perform(get("/games/"+gameId+"/lobby/delete")).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/" + gameId + "/lobby"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testDeleteLobbyWithoutCorrectUser() throws Exception {
        Game game=new Game();
        Integer gameId=1; 
        game.setId(gameId);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);

        User user=new User();
        Set<Game> games=new HashSet<Game>();
        
        user.setGames(games);
        Mockito.when(userService.authenticatedUser()).thenReturn(user);

        mockMvc.perform(get("/games/" + gameId + "/lobby/delete")).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/" + gameId + "/lobby"));
    }


    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testStartLobbyWithCorrectUser() throws Exception {
        Game game=new Game(); 
        Integer gameId=1;
        game.setId(gameId);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);

        User user=new User();
        Set<Game> games=new HashSet<Game>();
        games.add(game);
        user.setGames(games);
        Mockito.when(userService.authenticatedUser()).thenReturn(user);

        mockMvc.perform(get("/games/"+gameId+"/start")).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/" + gameId + "/lobby"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    public void testStartLobbyWithoutCorrectUser() throws Exception {
        Game game=new Game();
        Integer gameId=1; 
        game.setId(gameId);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);

        User user=new User();
        Set<Game> games=new HashSet<Game>();
        
        user.setGames(games);
        Mockito.when(userService.authenticatedUser()).thenReturn(user);

        mockMvc.perform(get("/games/"+gameId+"/start")).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/" + gameId + "/lobby"));
    }

    
}
