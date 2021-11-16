package org.springframework.samples.petclinic.userKoT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    
   private static final String VIEWS_USERS_KOT_CREATE_FORM = "userskot/createUserskotForm";

	@GetMapping()
    public String usersList(ModelMap modelMap){
        String view ="userskot/usersKoTList";
        Iterable<UserKoT> userskot= userService.findAll();
        modelMap.addAttribute("userskot", userskot);
        return view;
    }

    
}
