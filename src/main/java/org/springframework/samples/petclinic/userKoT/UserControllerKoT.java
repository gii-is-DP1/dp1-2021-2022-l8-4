package org.springframework.samples.petclinic.userKoT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sara Cruz
 * @author Rosa Molina
 */
@Controller
@RequestMapping("/userskot")
public class UserControllerKoT {

	@Autowired
	private UserServiceKoT userService;
    
   // private static final String VIEWS_USERS_CREATE_FORM = "users/usersList";

	@GetMapping()
    public String usersList(ModelMap modelMap){
        String view ="userskot/usersKoTList";
        Iterable<UserKoT> userskot= userService.findAll();
        modelMap.addAttribute("userskot", userskot);
        return view;
    }

    
}
