package org.springframework.samples.petclinic.userKoT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
public class UserController {

    private static final String VIEWS_OWNER_CREATE_FORM = "users/createOwnerForm";

	private final UserService userService;

	@Autowired
	public UserController(UserService clinicService) {
		this.userService = ;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_FORM;
		}
		else {
			//creating owner, user, and authority
			this.ownerService.saveOwner(owner);
			return "redirect:/";
		}
	}
}
