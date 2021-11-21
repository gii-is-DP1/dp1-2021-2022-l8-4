package org.springframework.samples.petclinic.user;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sara Cruz
 * @author Rosa Molina
 */
@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
    
   private static final String VIEW_WELCOME = "welcome";
   private static final String VIEWS_USERS_CREATE_UPDATE_FORM = "users/createOrUpdateUsersForm";

	@GetMapping()
    public String usersList(ModelMap modelMap){
        String view ="users/usersList";
        Iterable<User> users= userService.findAll();
        modelMap.addAttribute("users", users);
        return view;
    }

    /**
     * FORMULARIO PARA USUARIOS
     */    
    @GetMapping(path = "/new")
    public String initCreationForm(ModelMap modelMap) {
        String view = VIEWS_USERS_CREATE_UPDATE_FORM;
        modelMap.addAttribute("user", new User());
        return view;
    }

    @PostMapping(path = "/new")
    public String processCreationForm(@Valid User user, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()){
            modelMap.addAttribute("user", user);
            return VIEWS_USERS_CREATE_UPDATE_FORM;

        }else {
            //creating user
            userService.saveUser(user);
            modelMap.addAttribute("message","User succesfully created!");
            usersList(modelMap);
            return VIEW_WELCOME;
        }
    } 

    
    @GetMapping(value = "/{userId}/edit")
	public String initUpdateForm(@PathVariable("userId") int userId, ModelMap modelMap) {
		Optional<User> user = this.userService.findUserById(userId);
		modelMap.put("user", user);
		return VIEWS_USERS_CREATE_UPDATE_FORM;
	}

    /**
     *
     * @param user
     * @param result
     * @param userId
     * @param model
     * @return
     */

    @PostMapping(value = "/{userId}/edit")
	public String processUpdateForm(@Valid User user, BindingResult result, @PathVariable("userId") int userId, ModelMap modelMap) {
		if (result.hasErrors()) {
			modelMap.put("user", user);
			return VIEWS_USERS_CREATE_UPDATE_FORM;
		}
		else {
            Optional<User> userToUpdate=this.userService.findUserById(userId);
			BeanUtils.copyProperties(user, userToUpdate.get(), "id");                                                                                               
            this.userService.saveUser(userToUpdate.get());      
            modelMap.addAttribute("message","User succesfully edited!");              
			return "redirect:/users";
		}
	}
    
}
