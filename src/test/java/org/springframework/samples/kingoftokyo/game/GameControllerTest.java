package org.springframework.samples.kingoftokyo.game;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.configuration.CurrentUserController;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.gamecard.GameCardService;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;

/**
 * @author Jose Maria Delgado Sanchez
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = GameController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
            excludeAutoConfiguration = SecurityConfiguration.class)
public class GameControllerTest {

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

    @BeforeEach
    public void configureMock(){
        Game game = new Game();
        int gameId = 1;
        game.setId(gameId);
        Mockito.when(gameService.findGameById(gameId)).thenReturn(game);
    }

    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    @Disabled
    public void testOnGoingGameOk() throws Exception {
        mockMvc.perform(get("/games/1/playing"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/playing"));

    }
    
}
