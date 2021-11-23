package org.springframework.samples.petclinic.game;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.boardcard.BoardCardService;
import org.springframework.samples.petclinic.card.Card;

import org.springframework.samples.petclinic.dice.Roll;
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
 *  @author Ricardo Nadal Garcia 
 *  @author Jose Maria Delgado Sanchez
 */

 @Controller
 @RequestMapping("/games")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BoardCardService boardCardService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public String gameListNotFinished(ModelMap modelMap){
        if(userService.authenticatedUser() instanceof User){
            String view ="games/gamesList";
            Iterable<Game> games= gameService.findAllNotFinished();
            modelMap.addAttribute("games", games);
            return view;
        }
        return "redirect:/";
    }

    @GetMapping("/lobbies")
    public String lobbies(ModelMap modelMap){
        String view = "games/lobbiesList";
        List<Game> lobbies = gameService.findLobbies();
        modelMap.addAttribute("lobbies", lobbies);
        return view;
    }

    @GetMapping("/finished")
    public String gameListFinished(ModelMap modelMap){
        String view ="games/gamesListFinished";
        Iterable<Game> games= gameService.findAllFinished();
        modelMap.addAttribute("games", games);
        return view;
    }

    @GetMapping("/{gameId}/players")
    public String gamePlayers(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view="games/playersList";
        Iterable<Player> players= gameService.findPlayerList(gameId);
        Game game=gameService.findGameById(gameId);
        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        return view;
    }

    @GetMapping("/finished/{gameId}")
    public String gameFinished(ModelMap modelMap, @PathVariable("gameId") int gameId) {
        String view="games/playersList";
        Iterable<Player> players= gameService.findPlayerList(gameId);
        Game game=gameService.findGameById(gameId);
        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        return view;
    }

    @GetMapping("/{gameId}/roll") 
    public String gameRoll(ModelMap modelMap, @PathVariable("gameId") int gameId){
        String view ="games/roll";

        Iterable<Player> players= gameService.findPlayerList(gameId);
        Game game=gameService.findGameById(gameId);


        if(MapGameRepository.getInstance().getTurnList(gameId) == null) {
            List<Integer> turnList=gameService.initialTurnList(gameId);
            MapGameRepository.getInstance().putTurnList(gameId, turnList);
        }
       
        List<Integer> turnList=MapGameRepository.getInstance().getTurnList(gameId);
        Roll roll=MapGameRepository.getInstance().getRoll(gameId);

        String actualPlayerTurn=gameService.actualTurn(gameId).getMonsterName().toString();
        modelMap.addAttribute("actualPlayerTurn",actualPlayerTurn);

        Boolean isPlayerTurn=gameService.isPlayerTurn(gameId);
        modelMap.addAttribute("isPlayerTurn",isPlayerTurn);

        Boolean isPlayerInGame=gameService.isPlayerInGame(gameId);
        modelMap.addAttribute("isPlayerInGame",isPlayerInGame);

        modelMap.addAttribute("players",players);
        modelMap.addAttribute("game",game);
        modelMap.addAttribute("roll",roll);
        //Retrieve data from board_card association and generate a list of cards
        Set<Card> cards = boardCardService.findAvailableCardsByBoard(game.getBoard());
        modelMap.addAttribute("cards", cards);
        modelMap.addAttribute("turnList",turnList);

        return view;
    }

    @PostMapping("/{gameId}/roll")
    public String rollKeep(@ModelAttribute("newTurn") Boolean nuevoTurno,@ModelAttribute("roll") Roll roll,BindingResult result,ModelMap modelMap, @PathVariable("gameId") int gameId) throws DuplicatedMonsterNameException  {
        if(gameService.isPlayerTurn(gameId)) {
            if(nuevoTurno){
                gameService.nuevoTurno(gameId);
            } else{
                gameService.turnRoll(roll,gameId);
                if(roll.getRollAmount()==roll.getMaxThrows()) {
                    Integer playerIdActualTurn=gameService.actualTurnPlayerId(gameId);
                    playerService.useRoll(gameId,playerIdActualTurn,roll);
                    
                }
            }
        }
        
        return "redirect:/games/{gameId}/roll";
    }
    
    @GetMapping("/new") 
    public String newGame(ModelMap modelMap){
        if(userService.authenticatedUser() instanceof User){
            String view ="games/newGame";
            modelMap.addAttribute("newGame", new Game());
            return view;
        }else{
            return "redirect:/";
        }
    }
        
    @PostMapping("/new")
    public String createNewGame(@ModelAttribute("newGame") Game newGame, ModelMap modelMap){
        if(userService.authenticatedUser() instanceof User){
            User user = userService.authenticatedUser();
            gameService.createNewGame(user, newGame);
            
            return "redirect:/games/" + newGame.getId() + "/lobby";
        }else{
            return "redirect:/";
        }
    }

    @GetMapping("/{gameId}/lobby") 
    public String gameLobby(ModelMap modelMap, @PathVariable("gameId") int gameId){
        if(userService.authenticatedUser() instanceof User){
            Game game = gameService.findGameById(gameId);
            if(!game.isStarted()){
                String view ="games/lobby";

                modelMap.addAttribute("availableMonsters", game.availableMonsters());
                modelMap.addAttribute("game", game);
                modelMap.addAttribute("players", game.getPlayers());
                modelMap.addAttribute("newPlayer", new Player());
                return view;
            }
            return "redirect:/games";           
        }else{
            return "redirect:/";
        }
    }

    @PostMapping("/{gameId}/lobby")
    public String joinGame(@ModelAttribute("newPlayer") Player newPlayer, ModelMap modelMap, @PathVariable("gameId") int gameId){
        if(userService.authenticatedUser() instanceof User){
            User user = userService.authenticatedUser();
            Game game = gameService.findGameById(gameId);
            playerService.joinGame(user, newPlayer, game);

            return "redirect:/games/" + game.getId() + "/lobby";
        }
        
        return "redirect:/games";
    }

    @DeleteMapping("/{gameId}/lobby")
    public String deleteGame(ModelMap modelMap, @PathVariable("gameId") int gameId){
        if(userService.authenticatedUser() instanceof User){
            User user = userService.authenticatedUser();
            Game game = gameService.findGameById(gameId);
            gameService.deleteGameByCreator(user, game);  
        }
        
        return "redirect:/games";
    }


    @GetMapping("/{gameId}/start") 
    public String startGame(ModelMap modelMap, @PathVariable("gameId") int gameId){
        if(userService.authenticatedUser() instanceof User){
            User user = userService.authenticatedUser();
            Game game = gameService.findGameById(gameId);
            Boolean started = gameService.startGameByCreator(user, game);
            if(started){
                return "redirect:/games/" + game.getId() + "/roll";
            }else{
                return "redirect:/games/" + game.getId() + "/lobby";
            }
        }else{
            return "redirect:/games";
        }
     
    }
        
        
       
}
