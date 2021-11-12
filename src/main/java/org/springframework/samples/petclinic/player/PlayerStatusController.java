package org.springframework.samples.petclinic.player;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Noelia López Durán
 */
@Controller
public class PlayerStatusController {
    
    
	private PlayerService playerService;
	private static final String VIEWS_PLAYERSTATUS_CREATE_OR_UPDATE_FORM = "players/createOrUpdatePlayerStatusForm";

    /**
     * Copied from VisitController 
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2 goals:
	 * - Make sure we always have fresh data - Since we do not use the session scope, make
	 * sure that Pet object always has an id (Even though id is not part of the form
	 * fields)
	 * @param playerId
	 * @return Player
	 */
	@ModelAttribute("playerStatus")
	public PlayerStatus loadPlayerWithPlayerStatus(@PathVariable("playerId") int playerId) {
		Player player = this.playerService.findPlayerById(playerId);
		PlayerStatus playerStatus = new PlayerStatus();
		player.addPlayerStatus(playerStatus);
		return playerStatus;
	}

	@GetMapping(path = "/players/{playerId}/playerStatus/new")
	public String initNewStatusForm(@PathVariable("playerId") int playerId, ModelMap modelMap) {
		String view = VIEWS_PLAYERSTATUS_CREATE_OR_UPDATE_FORM;
		modelMap.addAttribute("playerStatus", new PlayerStatus());
		return view;
	}
	//Falta el post

	@GetMapping(path = "/players/{playerId}/playerStatus")
	public String showPlayerStatus(@PathVariable int playerId, Map<String, Object> modelMap) {
		modelMap.put("playerStatus", this.playerService.findPlayerById(playerId).getPlayerStatus());
		return "playerList";//To-Do
	}

}
