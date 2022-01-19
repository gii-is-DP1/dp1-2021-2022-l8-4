package org.springframework.samples.kingoftokyo.game;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.kingoftokyo.game.exceptions.DeleteGameException;
import org.springframework.samples.kingoftokyo.game.exceptions.NewGameException;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jose Maria Delgado Sanchez
 */

@Slf4j
@Controller
@RequestMapping("/games")
public class LobbyController {

    private final GameService gameService;
    private final UserService userService;
    private final PlayerService playerService;
    private static final String VIEWS_EXCEPTION = "exception";
    private static final String LOBBY = "/lobby";
    private static final String REDIRECTGAMES= "redirect:/games/";

    @Autowired
    public LobbyController(GameService gameService,
            UserService userService,
            PlayerService playerService) {
        this.gameService = gameService;
        this.userService = userService;
        this.playerService = playerService;
    }

    @InitBinder("newGame")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new NewGameValidator());
    }

    @GetMapping("/lobbies")
    public String lobbies(ModelMap modelMap) {
        String view = "games/lobbiesList";
        List<Game> lobbies = gameService.findLobbies();
        modelMap.addAttribute("lobbies", lobbies);
        return view;
    }

    @GetMapping("/new")
    public String newGame(ModelMap modelMap) {
        String view = "games/newGame";
        modelMap.addAttribute("newGame", new Game());
        return view;
    }

    @PostMapping("/new")
    public String createNewGame(@ModelAttribute("newGame") @Valid Game newGame, BindingResult result,
            ModelMap modelMap) {

        if (result.hasErrors()) {
            modelMap.put("newGame", newGame);
            return "games/newGame";
        } else {
            User user = userService.authenticatedUser();

            try {
                newGame.setCreator(user);
                gameService.createNewGame(newGame);
                return REDIRECTGAMES + newGame.getId() + LOBBY;
            } catch (NewGameException e) {
                log.warn(e.toString());
                return "redirect:/games/lobbies";
            }

        }
    }

    @GetMapping("/{gameId}/lobby")
    public String gameLobby(ModelMap modelMap, @PathVariable("gameId") int gameId, HttpServletResponse responde,
            @RequestParam(value = "message", required = false) final String message, @RequestParam(value = "messageType", required = false) final String messageType) {

        String view = "games/lobby";
        try {
            Game game = gameService.findGameById(gameId);
            if (game.isStarted()) {
                view = REDIRECTGAMES + game.getId() + "/playing";
            } else {
                responde.addHeader("Refresh", "10");

                modelMap.addAttribute("availableMonsters", game.availableMonsters());
                modelMap.addAttribute("game", game);
                modelMap.addAttribute("players", game.getPlayers());
                modelMap.addAttribute("newPlayer", new Player());

                if(message instanceof String && messageType instanceof String){
                    modelMap.put("messageType", messageType);
                    modelMap.put("message", message);
                }
            }
            return view;
        } catch (NotFoundException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }

    }

    @PostMapping("/{gameId}/lobby")
    public String joinGame(@ModelAttribute("newPlayer") @Valid Player newPlayer, BindingResult result,
            ModelMap modelMap, @PathVariable("gameId") int gameId, final RedirectAttributes redirectAttributes) {
        
        String view = REDIRECTGAMES + gameId + LOBBY;
        if (!result.hasErrors()) {

            User user = userService.authenticatedUser();
            newPlayer.setUser(user);

            try {
                Game game = gameService.findGameById(gameId);
                playerService.joinGame(newPlayer, game);
                return view;
            } catch (NotFoundException e) {
                log.warn(e.toString());
                return VIEWS_EXCEPTION;
            } catch (NewGameException e) {
                log.warn(e.toString());
                redirectAttributes.addAttribute("messageType", "danger");
                redirectAttributes.addAttribute("message", e.getMessage());
                return view;
            }
        }
        return view;
    }

    @GetMapping("/{gameId}/lobby/delete")
    public String deleteGame(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view = "redirect:/games/lobbies";
        User user = userService.authenticatedUser();

        try {
            Game game = gameService.findGameById(gameId);

            if (user.isCreator(game)) {
                gameService.deleteGame(game);
            } else {
                view = REDIRECTGAMES + gameId + LOBBY;
            }
        } catch (NotFoundException | DeleteGameException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }
        return view;
    }

    @GetMapping("/{gameId}/start")
    public String startGame(ModelMap modelMap, @PathVariable("gameId") int gameId, HttpServletResponse response,
            final RedirectAttributes redirectAttributes) {
        String view = REDIRECTGAMES + gameId + LOBBY;
        User user = userService.authenticatedUser();
        try {
            Game game = gameService.findGameById(gameId);
            if (user.isCreator(game)) {
                gameService.startGame(game);
            }
            return view;
        } catch (NotFoundException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        } catch (NewGameException e) {
            log.warn(e.toString());
            redirectAttributes.addAttribute("messageType", "danger");
            redirectAttributes.addAttribute("message", e.getMessage());
            return view;
        }
    }

}
