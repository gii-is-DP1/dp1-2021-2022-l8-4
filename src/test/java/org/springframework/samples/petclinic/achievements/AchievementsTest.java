package org.springframework.samples.petclinic.achievements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.modules.statistics.achievement.AchievementService;
import org.springframework.stereotype.Service;

/**
/* @author Carlos Varela Soult
*/

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchievementsTest {
    @Autowired
    private AchievementService achievementsService;

    @Test
    public void testCountWithInitialData() {
        int count = achievementsService.achievementsCount();
        assertEquals(count, 1);
    }

}
