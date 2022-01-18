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
                return "redirect:/games/" + newGame.getId() + "/lobby";
            } catch (NewGameException e) {
                log.warn(e.toString());
                return "redirect:/games/lobbies";
            }

        }
    }

    @GetMapping("/{gameId}/lobby")
    public String gameLobby(ModelMap modelMap, @PathVariable("gameId") int gameId, HttpServletResponse responde) {
        String view = "welcome";
        try {
            Game game = gameService.findGameById(gameId);
            if (game.isStarted()) {
                return "redirect:/games/" + game.getId() + "/playing";
            } else {
                view = "games/lobby";

                responde.addHeader("Refresh", "10");

                modelMap.addAttribute("availableMonsters", game.availableMonsters());
                modelMap.addAttribute("game", game);
                modelMap.addAttribute("players", game.getPlayers());
                modelMap.addAttribute("newPlayer", new Player());
                return view;
            }
        } catch (NotFoundException e) {
            log.warn(e.toString());
        }
        return view;
    }

    @PostMapping("/{gameId}/lobby")
    public String joinGame(@ModelAttribute("newPlayer") @Valid Player newPlayer, BindingResult result,
            ModelMap modelMap,
            @PathVariable("gameId") int gameId) {

        if (!result.hasErrors()) {

            User user = userService.authenticatedUser();
            newPlayer.setUser(user);

            try {
                Game game = gameService.findGameById(gameId);
                playerService.joinGame(newPlayer, game);
            } catch (NewGameException | NotFoundException e) {
                log.warn(e.toString());
            }

        }
        return "redirect:/games/" + gameId + "/lobby";
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
                view = "redirect:/games/" + gameId + "/lobby";
            }
        } catch (NotFoundException | DeleteGameException e) {
            log.warn(e.toString());
        }
        return view;
    }

    @GetMapping("/{gameId}/start")
    public String startGame(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view = "welcome";
        User user = userService.authenticatedUser();
        try {
            Game game = gameService.findGameById(gameId);
            if (user.isCreator(game)) {
                gameService.startGame(game);
            }
            return "redirect:/games/" + game.getId() + "/lobby";
        } catch (NotFoundException | NewGameException e) {
            log.warn(e.toString());
        }
        return view;
    }

}
