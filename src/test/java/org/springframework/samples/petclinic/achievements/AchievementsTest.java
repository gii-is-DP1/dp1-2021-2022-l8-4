package org.springframework.samples.petclinic.achievements;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.modules.statistics.achievement.Achievement;
import org.springframework.samples.petclinic.modules.statistics.achievement.AchievementService;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricType;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;

/**
/* @author Rosa Molina
/* @author Carlos Varela Soult
*/

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchievementsTest {
    @Autowired
    private AchievementService achievementsService;

    @Autowired
    private UserService userService;

    @Test
    public void testAchivementCreateandFind() {
        Achievement a = new Achievement();
        a.setId(2);
        a.setName("Achievement 2");
        a.setDescription("Nuevo achievement");
        a.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        a.setMetric(tipoMetrica);
        a.setRewardPoints(10);
        achievementsService.saveAchievement(a);
        Achievement a2 = achievementsService.findAchievementById(2).get();
        assertEquals(2, a2.getId());
        assertEquals("Achievement 2", a2.getName());
        assertEquals("Nuevo achievement", a2.getDescription());
        assertEquals(1l, a2.getGoal());
        assertEquals(tipoMetrica, a2.getMetric());
        assertEquals(10, a2.getRewardPoints());
    }


    @Test
    public void testFindAllAchievement(){
        List<Achievement> listcont= new ArrayList<>();
        achievementsService.findAll().forEach(listcont::add);
        Integer numero = 1;
        Integer contadorFind = listcont.size();
        assertEquals(contadorFind, numero);

        Achievement a = new Achievement();
        a.setId(3);
        a.setName("Achievement 3");
        a.setDescription("Nuevo achievement 3");
        a.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        a.setMetric(tipoMetrica);
        a.setRewardPoints(10);
        achievementsService.saveAchievement(a);

        List<Achievement> listcont2= new ArrayList<>();
        achievementsService.findAll().forEach(listcont2::add);
        numero = 2;
        contadorFind = listcont2.size();
        assertEquals(contadorFind, numero);
    }

    @Test
    public void testIsObtainAchievement(){
        Achievement a = new Achievement();
        a.setId(4);
        a.setName("Achievement 4");
        a.setDescription("Nuevo achievement 4");
        a.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        a.setMetric(tipoMetrica);
        a.setRewardPoints(10);
        achievementsService.saveAchievement(a);
        assertEquals(true, a.isObtained(1l));
    }


    @Test
    public void testUsersAchievement(){ //Como es tan largo lo he probado a parte
        Set<User> set = new HashSet<>();
        set.add(userService.findUserById(1).get());
        set.add(userService.findUserById(2).get());
        set.add(userService.findUserById(3).get());

        Achievement a = new Achievement();
        a.setId(5);
        a.setName("Achievement 5");
        a.setDescription("Nuevo achievement 5");
        a.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        a.setMetric(tipoMetrica);
        a.setRewardPoints(10);
        a.setUsers(set);
        achievementsService.saveAchievement(a);
        assertEquals(set, a.getUsers());
    }


}
