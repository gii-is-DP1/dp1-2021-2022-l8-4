package org.springframework.samples.petclinic.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserController {

    @Autowired
	private UserService userService;

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        User currentUser = userService.authenticatedUser();
        return currentUser;
}

}
