package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.springframework.samples.petclinic.userKoT.UserKoT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CurrentUserController {

    @GetMapping("/currentuser")
    public String showCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            if(authentication.isAuthenticated()){
                UserKoT currentUserKoT= (UserKoT)authentication.getPrincipal();
                System.out.println(currentUserKoT.getUsername());
            }
            else
                System.out.println("User not authenticated");
        }
        return "/welcome";
    }
}
