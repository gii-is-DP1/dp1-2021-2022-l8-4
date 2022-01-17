package org.springframework.samples.kingoftokyo.achievements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.game.GameService;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.Achievement;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;


/**
/* @author Rosa Molina
*/

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchievementServiceTest {
    @Autowired
    private AchievementService achievementsService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    /**
     * should create new achievement
     * set
     */

    @Test
    public void shouldCreateAchivement() {
        Achievement achievement = new Achievement();
        achievement.setId(2);
        achievement.setName("Achievement 2");
        achievement.setDescription("Nuevo achievement");
        achievement.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        achievement.setMetric(tipoMetrica);
        achievement.setRewardPoints(10);
        achievementsService.saveAchievement(achievement);
        assertThat(achievement.getId()).isNotNull();
    }

    /**
     * should get achievement by id
     * findAchivementById
     */

    @Test
    public void shouldGetAchivement(){
        Achievement achievement = achievementsService.findAchievementById(1).get();
        MetricType metrica = MetricType.gamesPlayed;
        assertEquals(1, achievement.getId());
        assertEquals("Mi primera partida", achievement.getName());
        assertEquals("Juega tu primera partida", achievement.getDescription());
        assertEquals(1l, achievement.getGoal());
        assertEquals(metrica, achievement.getMetric());
        assertEquals(10, achievement.getRewardPoints());
    }

    /**
     * should find all achievement
     * findall
     */


    @Test
    public void shouldFindAllAchievement(){
        List<Achievement> listcont= new ArrayList<>();
        achievementsService.findAll().forEach(listcont::add);
        Integer numero = 1;
        Integer contadorFind = listcont.size();
        assertEquals(contadorFind, numero);

        Achievement achievement = new Achievement();
        achievement.setId(3);
        achievement.setName("Achievement 3");
        achievement.setDescription("Nuevo achievement 3");
        achievement.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        achievement.setMetric(tipoMetrica);
        achievement.setRewardPoints(10);
        achievementsService.saveAchievement(achievement);

        List<Achievement> listcont2= new ArrayList<>();
        achievementsService.findAll().forEach(listcont2::add);
        numero = 2;
        contadorFind = listcont2.size();
        assertEquals(contadorFind, numero);
    }

    /**
     * should be obtain
     * isObtained
     */

    @Test
    public void shouldBeObtainAchievement(){
        Achievement achievement = new Achievement();
        achievement.setId(4);
        achievement.setName("Achievement 4");
        achievement.setDescription("Nuevo achievement 4");
        achievement.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        achievement.setMetric(tipoMetrica);
        achievement.setRewardPoints(10);
        achievementsService.saveAchievement(achievement);
        assertEquals(true, achievement.isObtained(1l));
    }

    /**
     * should get users achievement
     * getUsers
     */

    @Test
    public void shouldGetUsersAchievement(){
        Set<User> set = new HashSet<>();
        set.add(userService.findUserById(1).get());
        set.add(userService.findUserById(2).get());
        set.add(userService.findUserById(3).get());

        Achievement achievement = new Achievement();
        achievement.setId(5);
        achievement.setName("Achievement 5");
        achievement.setDescription("Nuevo achievement 5");
        achievement.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        achievement.setMetric(tipoMetrica);
        achievement.setRewardPoints(10);
        achievement.setUsers(set);
        achievementsService.saveAchievement(achievement);
        assertEquals(set, achievement.getUsers());
    }

    /**
     * should delete achievement
     * deleteAchievement
     */

    @Test
    public void shouldDeleteAchievement(){
        Achievement achievement = achievementsService.findAchievementById(1).get();
        assertEquals(achievement.getId(), 1);
        achievementsService.deleteAchievement(achievement);
        Optional emptyOptional = Optional.empty(); 
        assertEquals(achievementsService.findAchievementById(1), emptyOptional);
    }
    
    /**
     * should get the wins, cards, gamesPlayed
     */
    @Test
    public void shouldGetWinsByUser(){
        User user2 = userService.findUserById(2).get();
        MetricType metricType = MetricType.wins;
        Integer winsByUser = achievementsService.getScoreByUser(metricType, user2);
        assertEquals(winsByUser, 1);
    }

    @Test
    public void shouldGetGamesPlayedByUser(){
        User user1 = userService.findUserById(1).get();
        MetricType metricType = MetricType.gamesPlayed;
        Integer gamesPlayedByUser = achievementsService.getScoreByUser(metricType, user1);
        assertEquals(gamesPlayedByUser, 1);
    }

    @Test
    public void shouldGetCardsByUser(){
        User user1 = userService.findUserById(1).get();
        MetricType metricType = MetricType.cardsUsed;
        Integer cardUsedByUser = achievementsService.getScoreByUser(metricType, user1);
        assertEquals(cardUsedByUser, 0);
    }

    /**
     * should get scores by user
     * scoresByUser
     */

    @Test
    public void shouldGetScoresByUser(){
        User user1 = userService.findUserById(1).get();
        Map<MetricType, Integer> map = achievementsService.scoresByUser(user1);
        Map<MetricType, Integer> mapExpected = new HashMap<>();
        mapExpected.put(MetricType.gamesPlayed, 1);
        mapExpected.put(MetricType.wins, 0);
        mapExpected.put(MetricType.cardsUsed, 0);
        assertEquals(map, mapExpected);
    }

}
