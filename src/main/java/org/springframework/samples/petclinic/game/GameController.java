package org.springframework.samples.petclinic.game;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;

import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.gamecard.GameCardService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.player.exceptions.DuplicatedMonsterNameException;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Ricardo Nadal Garcia
 * @author Jose Maria Delgado Sanchez
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
        if (MapGameRepository.getInstance().getTurnList(gameId) == null) {
            List<Integer> turnList = gameService.initialTurnList(gameId);
            MapGameRepository.getInstance().putTurnList(gameId, turnList);
        }

        List<Integer> turnList = MapGameRepository.getInstance().getTurnList(gameId);
        Roll roll = MapGameRepository.getInstance().getRoll(gameId);

        Player actualPlayerTurn = gameService.actualTurn(gameId);
        modelMap.addAttribute("actualPlayerTurn", actualPlayerTurn);

        Boolean isPlayerTurn = gameService.isPlayerTurn(gameId);
        modelMap.addAttribute("isPlayerTurn", isPlayerTurn);

        if(!isPlayerTurn){
            responde.addHeader("Refresh", "1");
        }

        Boolean isPlayerInGame = gameService.isPlayerInGame(gameId);
        modelMap.addAttribute("isPlayerInGame", isPlayerInGame);

        Player actualPlayer=playerService.actualPlayer(gameId);
        modelMap.addAttribute("actualPlayer", actualPlayer);

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
    public String rollKeep(@ModelAttribute("newTurn") Boolean nuevoTurno, @ModelAttribute("roll") Roll roll,
            BindingResult result, ModelMap modelMap, @PathVariable("gameId") int gameId)
            throws DuplicatedMonsterNameException {
        if (gameService.isPlayerTurn(gameId)) {
            if (nuevoTurno) {
                gameService.nuevoTurno(gameId);
            } else {
                gameService.turnRoll(roll, gameId);
                if (roll.getRollAmount() == roll.getMaxThrows()) {
                    Integer playerIdActualTurn = gameService.actualTurnPlayerId(gameId);
                    playerService.useRoll(gameId, playerIdActualTurn, roll);

                }
            }
        }

        return "redirect:/games/{gameId}/playing";
    }

    @GetMapping("/new")
    public String newGame(ModelMap modelMap) {
        String view = "games/newGame";
        modelMap.addAttribute("newGame", new Game());
        return view;
    }

    @PostMapping("/new")
    public String createNewGame(@ModelAttribute("newGame") Game newGame, ModelMap modelMap) {
        User user = userService.authenticatedUser();
        Game game = gameService.createNewGame(user, newGame);
        if(game instanceof Game){
            return "redirect:/games/" + game.getId() + "/lobby";
        }else{
            return "redirect:/games/new";
        } 
    }

    @GetMapping("/{gameId}/lobby")
    public String gameLobby(ModelMap modelMap, @PathVariable("gameId") int gameId, HttpServletResponse responde) {
        Game game = gameService.findGameById(gameId);
        if (!game.isStarted()) {
            String view = "games/lobby";

            responde.addHeader("Refresh", "10");

            Boolean isCreator = game.getCreator() == userService.authenticatedUser();
            modelMap.addAttribute("isCreator", isCreator);

            modelMap.addAttribute("availableMonsters", game.availableMonsters());
            modelMap.addAttribute("game", game);
            modelMap.addAttribute("players", game.getPlayers());
            modelMap.addAttribute("newPlayer", new Player());
            return view;
        }
        return "redirect:/games/" + game.getId() + "/playing";
    }

    @PostMapping("/{gameId}/lobby")
    public String joinGame(@ModelAttribute("newPlayer") Player newPlayer, ModelMap modelMap,
            @PathVariable("gameId") int gameId) {

        User user = userService.authenticatedUser();
        Game game = gameService.findGameById(gameId);
        playerService.joinGame(user, newPlayer, game);

        return "redirect:/games/" + game.getId() + "/lobby";
    }

    @DeleteMapping("/{gameId}/lobby")
    public String deleteGame(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        User user = userService.authenticatedUser();
        Game game = gameService.findGameById(gameId);
        gameService.deleteGameByCreator(user, game);
        return "redirect:/games";
    }

    @GetMapping("/{gameId}/start")
    public String startGame(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        User user = userService.authenticatedUser();
        Game game = gameService.findGameById(gameId);
        Boolean started = gameService.startGameByCreator(user, game);
        if (started) {
            return "redirect:/games/" + game.getId() + "/playing";
        } else {
            return "redirect:/games/" + game.getId() + "/lobby";
        }
    }
    

}
