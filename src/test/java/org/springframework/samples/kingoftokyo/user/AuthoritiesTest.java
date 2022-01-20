package org.springframework.samples.kingoftokyo.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.stereotype.Service;

/**
 * @author Rosa Molina Arregui
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({SecurityConfiguration.class, MapGameRepository.class})
class AuthoritiesTest {
 
    
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
        Authorities auth = new Authorities();
        auth.setAuthority("prueba");
        authoritiesService.saveAuthorities(userService.findUserById(1).getId(), "prueba");
        Set<Authorities> auths = userService.findUserById(1).getAuthorities();
        for(Authorities a : auths){
            if(a.getAuthority().equals("prueba")){
                assertEquals(auth.getAuthority(), a.getAuthority());
            }
        }
    }


}
