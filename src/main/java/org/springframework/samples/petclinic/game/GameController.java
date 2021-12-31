package org.springframework.samples.petclinic.game;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.game.exceptions.NewGameCreationException;
import org.springframework.samples.petclinic.gamecard.GameCardService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
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

/**
 * @author Ricardo Nadal Garcia
 * @author Jose Maria Delgado Sanchez
 * @author Noelia López Durán
 */

@Controller
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameCardService gameCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private MapGameRepository mapGameRepository;

    @InitBinder("newGame")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new NewGameValidator());
	}

    @GetMapping()
    public String gameListNotFinished(ModelMap modelMap) {
        String view = "games/gamesList";
        Iterable<Game> games = gameService.findAllNotFinished();
        modelMap.addAttribute("games", games);
        return view;
    }

    @GetMapping("/lobbies")
    public String lobbies(ModelMap modelMap) {
        String view = "games/lobbiesList";
        List<Game> lobbies = gameService.findLobbies();
        modelMap.addAttribute("lobbies", lobbies);
        return view;
    }

    @GetMapping("/finished")
    public String gameListFinished(ModelMap modelMap) {
        String view = "games/gamesListFinished";
        Iterable<Game> games = gameService.findAllFinished();
        modelMap.addAttribute("games", games);
        return view;
    }

    @GetMapping("/{gameId}/players")
    public String gamePlayers(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view = "games/playersList";
        Iterable<Player> players = gameService.findPlayerList(gameId);
        Game game = gameService.findGameById(gameId);
        modelMap.addAttribute("players", players);
        modelMap.addAttribute("game", game);
        return view;
    }

    @GetMapping("/{gameId}/finished")
    public String gameFinished(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view = "games/gameFinished";
        Iterable<Player> players = gameService.findPlayerList(gameId);
        Game game = gameService.findGameById(gameId);
        modelMap.addAttribute("players", players);
        modelMap.addAttribute("game", game);
        return view;
    }

    @GetMapping("/{gameId}/playing")
    public String gameRoll(ModelMap modelMap, @PathVariable("gameId") int gameId, HttpServletResponse responde) {
        String view = "games/playing";

        gameService.endGame(gameId);

        Game game = gameService.findGameById(gameId);

        if (game.isFinished()) {
            return "redirect:/games/{gameId}/finished";
        }


        Iterable<Player> players = gameService.findPlayerList(gameId);
        
        if (mapGameRepository.getTurnList(gameId) == null) {
            List<Integer> turnList = gameService.initialTurnList(gameId);
            mapGameRepository.putTurnList(gameId, turnList);
        }

        List<Integer> turnList = mapGameRepository.getTurnList(gameId);
        Roll roll = mapGameRepository.getRoll(gameId);

        List<Player> orderedPlayers = gameService.playersOrder(turnList);
        modelMap.addAttribute("orderedPlayers", orderedPlayers);

        Player actualPlayerTurn = gameService.actualTurn(gameId);
        modelMap.addAttribute("actualPlayerTurn", actualPlayerTurn);

        Boolean isPlayerTurn = gameService.isPlayerTurn(gameId);
        modelMap.addAttribute("isPlayerTurn", isPlayerTurn);

        if (!isPlayerTurn) {
            responde.addHeader("Refresh", "1");
        }

        Boolean isPlayerInGame = gameService.isPlayerInGame(gameId);
        modelMap.addAttribute("isPlayerInGame", isPlayerInGame);

        Player actualPlayer = playerService.actualPlayer(gameId);
        modelMap.addAttribute("actualPlayer", actualPlayer);

        Player AuthenticatedPlayer = gameService.playerInGameByUser(userService.authenticatedUser(), gameId);
        modelMap.addAttribute("AuthenticatedPlayer", AuthenticatedPlayer);

        modelMap.addAttribute("players", players);
        modelMap.addAttribute("game", game);
        modelMap.addAttribute("roll", roll);
        // Retrieve data from board_card association and generate a list of cards
        List<Card> cards = gameCardService.findAvailableCardsByGame(game);
        modelMap.addAttribute("cards", cards);
        modelMap.addAttribute("turnList", turnList);

        return view;
    }

    @PostMapping("/{gameId}/playing")
    public String rollKeep(@ModelAttribute("newTurn") Boolean newTurn, @ModelAttribute("roll") Roll roll,
            @PathVariable("gameId") int gameId) {

        gameService.handleTurnAction(gameId, newTurn, roll);

        return "redirect:/games/{gameId}/playing";
    }

    @GetMapping("/{gameId}/exitTokyo")
    public String exitTokyo(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        gameService.handleExitTokyo(gameId);
        return "redirect:/games/{gameId}/playing";
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

            try{
                newGame.setCreator(user);
                gameService.createNewGame(newGame);
                return "redirect:/games/" + newGame.getId() + "/lobby";
            } catch (NewGameCreationException e) {
                return "redirect:/games/new";
            }

        }
    }

    @GetMapping("/{gameId}/lobby")
    public String gameLobby(ModelMap modelMap, @PathVariable("gameId") int gameId, HttpServletResponse responde) {
        Game game = gameService.findGameById(gameId);

        if (game.isStarted()) {
            return "redirect:/games/" + game.getId() + "/playing";
        } else {
            String view = "games/lobby";

            responde.addHeader("Refresh", "10");

            modelMap.addAttribute("availableMonsters", game.availableMonsters());
            modelMap.addAttribute("game", game);
            modelMap.addAttribute("players", game.getPlayers());
            modelMap.addAttribute("newPlayer", new Player());
            return view;
        }
    }

    @PostMapping("/{gameId}/lobby")
    public String joinGame(@ModelAttribute("newPlayer") @Valid Player newPlayer, BindingResult result, ModelMap modelMap,
            @PathVariable("gameId") int gameId) {

        if(!result.hasErrors()){
            Game game = gameService.findGameById(gameId);
            User user = userService.authenticatedUser();
            newPlayer.setUser(user);
            playerService.joinGame(newPlayer, game);
        }
        return "redirect:/games/" + gameId + "/lobby";
    }

    @GetMapping("/{gameId}/lobby/delete")
    public String deleteGame(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        User user = userService.authenticatedUser();
        Game game = gameService.findGameById(gameId);

        if(user.isCreator(game)){
            gameService.deleteGame(game);
            return "redirect:/games/lobbies";
        }else{
            return "redirect:/games/" + gameId + "/lobby"; 
        }
        
    }

    @GetMapping("/{gameId}/start")
    public String startGame(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        User user = userService.authenticatedUser();
        Game game = gameService.findGameById(gameId);

        if (user.isCreator(game)) {
            gameService.startGame(game);
            if(game.isStarted()){
                return "redirect:/games/" + game.getId() + "/playing";
            }
        } 

        return "redirect:/games/" + game.getId() + "/lobby";
        
    }

}
