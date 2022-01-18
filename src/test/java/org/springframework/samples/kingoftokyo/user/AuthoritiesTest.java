package org.springframework.samples.kingoftokyo.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * @author Rosa Molina Arregui
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class AuthoritiesTest {
 
    
    @Autowired
    private UserService userService;
    @Autowired
    private AuthoritiesService authoritiesService;

    @Test
    void createInitialPlayer() {
        Authorities auth = new Authorities();
        auth.setAuthority("prueba");
        authoritiesService.saveAuthorities(auth);
        assertEquals("prueba", auth.authority);
    }

    @Test
    void createAuthWithPlayer(){
        authoritiesService.saveAuthorities(1, "prueba");
        Authorities auth = new Authorities();
        auth.setAuthority("prueba");
        authoritiesService.saveAuthorities(auth);
        assertThat(userService.findUserById(1).get().getAuthorities().contains(auth));
    }

}
