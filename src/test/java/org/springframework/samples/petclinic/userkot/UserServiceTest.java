package org.springframework.samples.petclinic.userkot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.userKoT.UserServiceKoT;
import org.springframework.stereotype.Service;

/**
 * @author Carlos Varela Soult
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class UserServiceTest {
    
    @Autowired
    private UserServiceKoT userService;

    //TODOLos demás tests

    @Test
    public void testGetCurrentUserId() {
        Integer currentUserId = userService.getCurrentUserId("user2");
        assertThat(currentUserId).isEqualTo(2);
    }


}
