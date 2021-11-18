package org.springframework.samples.petclinic.player;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Noelia López Durán
 */
@Controller
public class PlayerStatusController {
    
    
	private final PlayerService playerService;
	private static final String VIEWS_PLAYERSTATUS_CREATE_OR_UPDATE_FORM = "players/createOrUpdatePlayerStatusForm";

	@Autowired
	public PlayerStatusController(PlayerService playerServ) {
		this.playerService = playerServ;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
    /**
     * Copied from VisitController 
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2 goals:
	 * - Make sure we always have fresh data - Since we do not use the session scope, make
	 * sure that Pet object always has an id (Even though id is not part of the form
	 * fields)
	 * @param playerId
	 * @return Player
	 */
	@ModelAttribute("statusTypes")
	public Collection<StatusType> populateStatusTypes() {
		return this.playerService.findStatusTypes();
	}

	@GetMapping(path = "/players/{playerId}/playerStatus/new")
	public String initNewStatusForm(@PathVariable("playerId") int playerId, ModelMap modelMap) {
		String view = VIEWS_PLAYERSTATUS_CREATE_OR_UPDATE_FORM;
		Player player = this.playerService.findPlayerById(playerId);
		modelMap.put("player",player);
		modelMap.addAttribute("playerStatus", new PlayerStatus());
		return view;
	}
	@PostMapping(path = "/players/{playerId}/playerStatus/new")
	public String processNewStatusForm(@Valid PlayerStatus pStatus, BindingResult result,ModelMap modelMap) {
		String view = VIEWS_PLAYERSTATUS_CREATE_OR_UPDATE_FORM;
		if (result.hasErrors()) {
			modelMap.addAttribute("playerStatus", pStatus);
			return view;
		}
		else {
			this.playerService.savePlayerStatus(pStatus);
			return "redirect:/players/{playerId}/playerStatus/";
		}
	}

	@GetMapping(path = "/players/{playerId}/playerStatus")
	public String showPlayerStatus(@PathVariable int playerId, Map<String, Object> modelMap) {
		String view = "players/playerStatusList";
		Player player = this.playerService.findPlayerById(playerId);
		List<PlayerStatus> lsStatus =  player.getPlayerStatusList();
		modelMap.put("player",player);
		modelMap.put("playerStatus", lsStatus );
		return view;	
	}

}
