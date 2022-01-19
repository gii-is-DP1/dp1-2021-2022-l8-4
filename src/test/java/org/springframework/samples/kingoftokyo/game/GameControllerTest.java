package org.springframework.samples.kingoftokyo.game;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.configuration.CurrentUserController;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.gamecard.GameCardService;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.player.exceptions.InvalidPlayerActionException;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javassist.NotFoundException;

import org.springframework.context.annotation.FilterType;

/**
 * @author Jose Maria Delgado Sanchez
 */

@WebMvcTest(value = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private CurrentUserController currentUserController;

    @MockBean
    private UserService userService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private MapGameRepository mapGameRepository;

    @MockBean
    private GameCardService gameCardService;

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testGameListNotFinishedOk() throws Exception {
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/gamesList"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testGameListFinishedOk() throws Exception {
        mockMvc.perform(get("/games/finished"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/gamesListFinished"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testGameFinishedOk() throws Exception {

        Mockito.when(gameService.findGameById(anyInt())).thenReturn(new Game());

        mockMvc.perform(get("/games/{gameId}/finished", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/gameFinished"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testGameFinishedFail() throws Exception {

        doThrow(new NotFoundException("")).when(gameService).findGameById(anyInt());

        mockMvc.perform(get("/games/{gameId}/finished", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testOnGoingGameOk() throws Exception {

        Mockito.when(gameService.findGameById(anyInt())).thenReturn(new Game());

        mockMvc.perform(get("/games/{gameId}/playing", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/playing"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testOnGoingGameFail() throws Exception {

        doThrow(new NotFoundException("")).when(gameService).findGameById(anyInt());

        mockMvc.perform(get("/games/{gameId}/playing", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testFinishedGameRedirectFromPlaying() throws Exception {

        Game game = new Game();
        game.setWinner("winner");
        Mockito.when(gameService.findGameById(anyInt())).thenReturn(game);

        mockMvc.perform(get("/games/{gameId}/playing", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/1/finished"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testExitTokyoOk() throws Exception {

        mockMvc.perform(get("/games/{gameId}/exitTokyo", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/1/playing"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testExitTokyoFail() throws Exception {

        doThrow(new NotFoundException("")).when(gameService).findGameById(anyInt());

        mockMvc.perform(get("/games/{gameId}/exitTokyo", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    @Disabled
    void testRollKeepOk() throws Exception {
        Mockito.when(gameService.findGameById(anyInt())).thenReturn(new Game());
        mockMvc.perform(post("/games/1/playing")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/1/playing"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    @Disabled
    void testRollKeepFailNoGame() throws Exception {
        doThrow(new NotFoundException("")).when(gameService).findGameById(anyInt());

        mockMvc.perform(post("/games/{gameId}/playing", "1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    @Disabled
    void testRollKeepFailInvalidAction() throws Exception {

        Mockito.when(gameService.findGameById(anyInt())).thenReturn(new Game());
        doThrow(new InvalidPlayerActionException("")).when(gameService).handleTurnAction(any(Game.class), anyBoolean(),
                any(Roll.class));

        mockMvc.perform(post("/games/{gameId}/playing", "1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/1/playing"));
    }

}
