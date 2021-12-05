package org.springframework.samples.petclinic.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserController {

    @Autowired
	private UserService userService;
    
    @ModelAttribute("user")
    public org.springframework.samples.petclinic.user.User getCurrentUser() {
        org.springframework.samples.petclinic.user.User currentUserkot = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth!=null && auth.isAuthenticated())
            currentUser = (User) auth.getPrincipal();
            String cUser = currentUser.getUsername();
            currentUserkot = userService.findUserByUsername(cUser);
        return currentUserkot;
    }
}
