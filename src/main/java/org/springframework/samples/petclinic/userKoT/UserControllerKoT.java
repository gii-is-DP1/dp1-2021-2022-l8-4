package org.springframework.samples.petclinic.userKoT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Sara Cruz
 * @author Rosa Molina
 */
@Controller
public class UserControllerKoT {

	@Autowired
	private UserServiceKoT userService;
    
   // private static final String VIEWS_USERS_CREATE_FORM = "users/usersList";

	@GetMapping()
    public String usersList(ModelMap modelMap){
        String view ="userskot/usersList";
        Iterable<UserKoT> users= userService.findAll();
        modelMap.addAttribute("users", users);
        return view;
    }
}
