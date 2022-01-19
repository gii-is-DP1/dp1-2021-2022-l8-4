package org.springframework.samples.kingoftokyo.game;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.gamecard.GameCardService;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.player.exceptions.InvalidPlayerActionException;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ricardo Nadal Garcia
 * @author Jose Maria Delgado Sanchez
 * @author Noelia López Durán
 */
@Slf4j
@Controller
@RequestMapping("/games")
public class GameController {

    private GameService gameService;
    private PlayerService playerService;
    private GameCardService gameCardService;
    private UserService userService;
    private MapGameRepository mapGameRepository;
    private String viewGames = "redirect:/games/";
    private static final String VIEWS_EXCEPTION = "exception";

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

    @GetMapping("/{gameId}/finished")
    public String gameFinished(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view = "games/gameFinished";
        try {
            Game game = gameService.findGameById(gameId);
            Iterable<Player> players = game.getPlayers();
            modelMap.addAttribute("players", players);
            modelMap.addAttribute("game", game);
        } catch (NotFoundException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }
        return view;
    }

    @GetMapping("/{gameId}/playing")
    public String gameRoll(ModelMap modelMap, @PathVariable("gameId") int gameId, HttpServletResponse responde) {
        String view = "games/playing";

        try {
            Game game = gameService.findGameById(gameId);
            if (game.isFinished()) {
                return viewGames + gameId + "/finished";
            }
            gameService.endGame(game);

            if (mapGameRepository.getTurnList(gameId) == null) {
                List<Integer> turnList = gameService.initialTurnList(game);
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

            Boolean isPlayerInGame = gameService.isPlayerInGame(game);
            modelMap.addAttribute("isPlayerInGame", isPlayerInGame);

            Player actualPlayer = playerService.actualPlayer(game);
            modelMap.addAttribute("actualPlayer", actualPlayer);

            Iterable<Player> players = game.getPlayers();
            modelMap.addAttribute("players", players);
            modelMap.addAttribute("game", game);
            modelMap.addAttribute("roll", roll);
            // Retrieve data from board_card association and generate a list of cards
            List<Card> cards = gameCardService.findAvailableCardsByGame(game);
            modelMap.addAttribute("cards", cards);
            modelMap.addAttribute("turnList", turnList);
            Player authenticatedPlayer = gameService.playerInGameByUser(userService.authenticatedUser(), gameId);
            modelMap.addAttribute("AuthenticatedPlayer", authenticatedPlayer);

        } catch (NotFoundException e) {
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }

        return view;
    }

    @PostMapping("/{gameId}/playing")
    public String rollKeep(@ModelAttribute("newTurn") Boolean newTurn, @ModelAttribute("roll") Roll roll,
            @PathVariable("gameId") int gameId) {

        try{
            Game game = gameService.findGameById(gameId);
            gameService.handleTurnAction(game, newTurn, roll);
        }catch(NotFoundException e){
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        } catch (InvalidPlayerActionException e) {
            log.warn(e.toString());
        }
        return viewGames + gameId + "/playing";
    }

    @GetMapping("/{gameId}/exitTokyo")
    public String exitTokyo(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        try{
            Game game = gameService.findGameById(gameId);
            gameService.handleExitTokyo(game);
        }catch(NotFoundException e){
            log.warn(e.toString());
            return VIEWS_EXCEPTION;
        }
        return viewGames + gameId + "/playing";
    }

}
