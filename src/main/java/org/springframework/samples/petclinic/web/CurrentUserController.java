package org.springframework.samples.petclinic.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Carlos Varela Soult
 */

@Controller
public class CurrentUserController {

    @GetMapping("/currentuser")
    public String getCurrentUserUsername() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null) {
            if(authentication.isAuthenticated() && authentication.getPrincipal() instanceof User){
                User currentUser = (User) authentication.getPrincipal();
                username = currentUser.getUsername();
                System.out.println(username);
            } else {
                System.out.println("User not authenticated");
                System.out.println(username);
            }
        }
        return username;
    }
}
