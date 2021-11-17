package org.springframework.samples.petclinic.userKoT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/userskot")
public class UserControllerKoT {

	@Autowired
	private UserServiceKoT userService;
    
   private static final String VIEWS_USERS_KOT_CREATE_UPDATE_FORM = "userskot/createUserskotForm";

	@GetMapping()
    public String usersList(ModelMap modelMap){
        String view ="userskot/usersKoTList";
        Iterable<UserKoT> userskot= userService.findAll();
        modelMap.addAttribute("userskot", userskot);
        return view;
    }

    /**
     * FORMULARIO PARA USUARIOS
     */    
    @GetMapping(path = "/new")
    public String initCreationForm(ModelMap modelMap) {
        String view = VIEWS_USERS_KOT_CREATE_UPDATE_FORM;
        modelMap.addAttribute("userkot", new UserKoT());
        return view;
    }

    @PostMapping(path = "/new")
    public String processCreationForm(@Valid UserKoT userkot, BindingResult result, ModelMap modelMap) {
        String view;
        if (result.hasErrors()){
            modelMap.addAttribute("userkot", userkot);
            return VIEWS_USERS_KOT_CREATE_UPDATE_FORM;

        }else {
            //creating user
            userService.saveUser(userkot);
            modelMap.addAttribute("message","User succesfully created!");
            view = usersList(modelMap);
        }
        return "redirect:";
    } 

    @GetMapping(value = "/{userId}/edit")
	public String initUpdateForm(@PathVariable("userId") int userId, ModelMap modelMap) {
		Optional<UserKoT> user = this.userService.findUserkotById(userId);
		modelMap.put("user", user.get());
		return VIEWS_USERS_KOT_CREATE_UPDATE_FORM;
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
	public String processUpdateForm(@Valid UserKoT user, BindingResult result, @PathVariable("userId") int userId, ModelMap modelMap) {
		if (result.hasErrors()) {
			modelMap.put("user", user);
			return VIEWS_USERS_KOT_CREATE_UPDATE_FORM;
		}
		else {
            Optional<UserKoT> userToUpdate=this.userService.findUserkotById(userId);
			BeanUtils.copyProperties(user, userToUpdate.get(), "id");                                                                                                    
            this.userService.saveUser(userToUpdate.get());                    
			return "redirect:/userskot";
		}
	}
    
}
