package org.springframework.samples.kingoftokyo.user;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;


/**
 * @author Sara Cruz
 * @author Rosa Molina
 * @author Carlos Varela Soult
 */
@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private static final String VIEWS_USERS_UPDATE_FORM = "users/updateUsersForm";
    private static final String VIEWS_USERS_CREATE_FORM = "users/createUsersForm";
    private static final String VIEW_USER_LIST = "users/usersList";
    private static final String VIEWS_EXCEPTION = "exception";
    private String message = "message";

    private UserService userService;
    private AchievementService achievementService;
    @Autowired
    public UserController(UserService userService, AchievementService achievementService) {
        this.userService = userService;
        this.achievementService = achievementService;
    }

    /**
     * @param modelMap
     * @param page
     * @return View of paginated list of users
     */

    @GetMapping()
    public String usersList(ModelMap modelMap, @RequestParam(value = "page", defaultValue = "1") int page) {
        String view = VIEW_USER_LIST;
        Page<User> pages = userService.getPageOfUsers(page - 1);
        modelMap.addAttribute("totalPages", pages.getTotalPages());
        modelMap.addAttribute("totalElements", pages.getTotalElements());
        modelMap.addAttribute("number", pages.getNumber());
        modelMap.addAttribute("users", pages.getContent());
        modelMap.addAttribute("size", pages.getContent().size());
        return view;
    }

    @GetMapping(path = "/new")
    public String initCreationForm(ModelMap modelMap) {
        String view = VIEWS_USERS_CREATE_FORM;
        modelMap.addAttribute("user", new User());
        modelMap.put("maxTurns", 0l);
        return view;
    }

    @PostMapping(path = "/new")
    public String processCreationForm(@Valid User user, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            modelMap.addAttribute("user", user);
            modelMap.put("maxTurns", 0l);
            return VIEWS_USERS_CREATE_FORM;

        } else {
            // creating user
            userService.saveUser(user,false);
            modelMap.addAttribute(message, "User succesfully created!");
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/{userId}/edit")
    public String initUpdateForm(@PathVariable("userId") int userId, ModelMap modelMap) {

        Integer currentUserId = userService.authenticatedUser().getId();
        if (currentUserId.equals(userId) || userService.isAdmin(currentUserId)) {
            User user = this.userService.findUserById(userId);
            modelMap.put("user", user);
            modelMap.put("maxTurns", user.getMaxTurnsTokyo());
            return VIEWS_USERS_UPDATE_FORM;

        } else {
            return VIEWS_EXCEPTION;
        }

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
    public String processUpdateForm(@Valid User user, BindingResult result, @PathVariable("userId") int userId,
            ModelMap modelMap, @RequestParam(value = "version", required=false) Integer version,
             @RequestParam(value = "newPassword") String newPassword, @RequestParam(value = "oldPassword") String oldPassword, HttpServletResponse response) {

        if(user.getVersion()!=version) { 
            modelMap.put("message","Ha habido una modificación del usuario mientras lo editabas! Prueba de nuevo!");
            modelMap.put("user", user);
            modelMap.put("maxTurns", user.getMaxTurnsTokyo());
            return VIEWS_USERS_UPDATE_FORM;
        }else if (result.hasErrors()) {
            modelMap.put("user", user);
            modelMap.put("maxTurns", user.getMaxTurnsTokyo());
            return VIEWS_USERS_UPDATE_FORM;
        } else {
            User userToUpdate = this.userService.findUserById(userId);
            BeanUtils.copyProperties(user, userToUpdate, "id");
            try {
                userToUpdate=userService.passwordCheckEdit(oldPassword, newPassword, userToUpdate);
            } catch (Exception e) {
                modelMap.put("user", user);
                modelMap.put("maxTurns", user.getMaxTurnsTokyo());
                modelMap.put("messageType","danger");
                modelMap.put(message,"Contraseña antigua erronea!");
                return VIEWS_USERS_UPDATE_FORM;
            }
            this.userService.saveUser(userToUpdate,true);
            modelMap.addAttribute("message", "Usuario editado correctamente");
            return "redirect:/users/profile/{userId}";
        }
    }

    @GetMapping(value = "/profile/{userId}")
    public String usersProfile(@PathVariable("userId") int userId, ModelMap modelMap) {
        String view = "users/profile";
        User user = this.userService.findUserById(userId);

        achievementService.checkAchievements(user);

        modelMap.addAttribute("user", user);
        return view;
    }

    @GetMapping(path = "/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId, ModelMap modelMap) {
        
        User user = userService.findUserById(userId);
        userService.deleteUser(user);
        modelMap.addAttribute(message, "User succesfully deleted");

        return "redirect:/users?page=1";
    }
}
