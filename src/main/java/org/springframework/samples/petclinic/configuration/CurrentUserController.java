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

    @ModelAttribute("currentUser")
    public org.springframework.samples.petclinic.user.User getCurrentUser() {
        org.springframework.samples.petclinic.user.User currentUserKot =null;
        User currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)){            
            currentUser = (User) auth.getPrincipal();
            String cUsername = currentUser.getUsername();
            currentUserKot = userService.findUserByUsername(cUsername);
        }
        return currentUserKot;
}
    
}
