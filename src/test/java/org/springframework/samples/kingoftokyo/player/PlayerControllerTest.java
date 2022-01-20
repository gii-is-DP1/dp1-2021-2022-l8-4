package org.springframework.samples.kingoftokyo.player;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.card.CardService;
import org.springframework.samples.kingoftokyo.configuration.CurrentUserController;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.playercard.PlayerCardService;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


/**
 * @author Sara Cruz
 */
@WebMvcTest(value = PlayerController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)

class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrentUserController currentUserController;
    
    @MockBean
    private UserService userService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PlayerCardService playerCardService;

    @MockBean
    private CardService cardService;

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test 
    void testPlayerList() throws Exception {
        mockMvc.perform(get("/players")).andExpect(status().isOk())
                .andExpect(view().name("players/playersList"))
                .andExpect(model().attributeExists("players"));
    }

    @WithMockUser(value = "spring", authorities = {"user"})
    @Test 
    void testBuyCard() throws Exception {
        Game game = new Game();
        Integer gameId = 1;
        game.setId(gameId);

        Player player = new Player();
        Integer playerId = 1;
        player.setId(playerId);
        player.setGame(game);
        Mockito.when(playerService.findPlayerById(playerId)).thenReturn(player);

        Card card = new Card();
        Integer cardId = 1;
        card.setId(cardId);
        Mockito.when(cardService.findCardById(cardId)).thenReturn(card);

        mockMvc.perform(get("/players/"+playerId+"/cards/"+cardId+"/buy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/"+gameId+"/playing"));
    }

    @WithMockUser(value = "spring", authorities = {"user"})
    @Test 
    void testBuyCardThrowingException() throws Exception {
        Player player = new Player();
        Integer playerId = 1;
        player.setId(playerId);
        Mockito.when(playerService.findPlayerById(playerId)).thenReturn(player);

        Card card = new Card();
        Integer cardId = 1;
        card.setId(cardId);
        Mockito.when(cardService.findCardById(cardId)).thenReturn(card);

        mockMvc.perform(get("/players/"+playerId+"/cards/"+cardId+"/buy"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", authorities = {"user"})
    @Test 
    void testDiscardAllCards() throws Exception {
        Game game = new Game();
        Integer gameId = 1;
        game.setId(gameId);

        Player player = new Player();
        Integer playerId = 1;
        player.setId(playerId);
        player.setGame(game);
        Mockito.when(playerService.findPlayerById(playerId)).thenReturn(player);

        mockMvc.perform(get("/players/"+playerId+"/cards/discard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/"+gameId+"/playing"));
    }

    @WithMockUser(value = "spring", authorities = {"user"})
    @Test 
    void testDiscardAllCardsThrowingException() throws Exception {
        Player player = new Player();
        Integer playerId = 1;
        player.setId(playerId);
        Mockito.when(playerService.findPlayerById(playerId)).thenReturn(player);

        mockMvc.perform(get("/players/"+playerId+"/cards/discard"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"));
    }
    
    @WithMockUser(value = "spring", authorities = {"user"})
    @Test 
    void testSurrender() throws Exception {
        Game game = new Game();
        Integer gameId = 1;
        game.setId(gameId);

        Player player = new Player();
        Integer playerId = 1;
        player.setId(playerId);
        player.setGame(game);
        Mockito.when(playerService.findPlayerById(playerId)).thenReturn(player);

        mockMvc.perform(get("/players/"+playerId+"/surrender"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/games/"+gameId+"/playing"));
    }

    @WithMockUser(value = "spring", authorities = {"user"})
    @Test 
    void testSurrenderThrowingException() throws Exception {
        Player player = new Player();
        Integer playerId = 1;
        player.setId(playerId);
        Mockito.when(playerService.findPlayerById(playerId)).thenReturn(player);

        mockMvc.perform(get("/players/"+playerId+"/surrender"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"));
    }

}
