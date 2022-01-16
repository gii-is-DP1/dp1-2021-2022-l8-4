package org.springframework.samples.kingoftokyo.game;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.gamecard.GameCardService;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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

    private GameService gameService;
    private PlayerService playerService;
    private GameCardService gameCardService;
    private UserService userService;
    private MapGameRepository mapGameRepository;

    @Autowired
    public GameController(GameService gameService,
            PlayerService playerService,
            GameCardService gameCardService,
            UserService userService,
            MapGameRepository mapGameRepository) {
                this.gameService = gameService;
                this.playerService = playerService;
                this.gameCardService = gameCardService;
                this.userService = userService;
                this.mapGameRepository = mapGameRepository;
    }


    @GetMapping()
    public String gameListNotFinished(ModelMap modelMap) {
        String view = "games/gamesList";
        Iterable<Game> games = gameService.findAllNotFinished();
        modelMap.addAttribute("games", games);
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


}
