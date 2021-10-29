package org.springframework.samples.petclinic.player;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Noelia López Durán
 */
@Controller
public class PlayerStatusController {
    
    
	private final PlayerService playerService;

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
		PlayerStatus playerStatus = new playerStatus();
		player.addPlayerStatus(playerStatus);
		return playerStatus;
	}
}
