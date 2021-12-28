package org.springframework.samples.petclinic.achievements;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.tomcat.jni.Local;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.modules.statistics.achievement.Achievement;
import org.springframework.samples.petclinic.modules.statistics.achievement.AchievementService;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricService;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricType;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;

import javassist.bytecode.Opcode;

/**
/* @author Rosa Molina
/* @author Carlos Varela Soult
*/

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchievementsTest {
    @Autowired
    private AchievementService achievementsService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    
    @Test
    public void testAchivementCreate() {
        Achievement achievement = new Achievement();
        achievement.setId(2);
        achievement.setName("Achievement 2");
        achievement.setDescription("Nuevo achievement");
        achievement.setGoal(1l);
        MetricType tipoMetrica = MetricType.wins;
        achievement.setMetric(tipoMetrica);
        achievement.setRewardPoints(10);
        achievementsService.saveAchievement(achievement);
    }

    @Test
    public void testAchivementGet(){
        Achievement achievement = achievementsService.findAchievementById(1).get();
        MetricType metrica = MetricType.gamesPlayed;
        assertEquals(1, achievement.getId());
        assertEquals("Mi primera partida", achievement.getName());
        assertEquals("Juega tu primera partida", achievement.getDescription());
        assertEquals(1l, achievement.getGoal());
        assertEquals(metrica, achievement.getMetric());
        assertEquals(10, achievement.getRewardPoints());
    }


    @Test
    public void testFindAllAchievement(){
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

    @Test
    public void testIsObtainAchievement(){
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


    @Test
    public void testUsersAchievement(){ //Como es tan largo lo he probado achievement parte
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


    @Test
    public void testCheckAndDeleteAchievement(){
        Achievement achievement = achievementsService.findAchievementById(1).get();
        assertEquals(achievement.getId(), 1);
        achievementsService.deleteAchievement(achievement);
        Optional emptyOptional = Optional.empty(); 
        assertEquals(achievementsService.findAchievementById(1), emptyOptional);
    }
    
    @Test
    public void testWinsandCardByUserAchievement(){
        User user1 = userService.findUserById(1).get();
        Integer cardUsedByUser = achievementsService.cardsUsedByUser(user1);
        assertEquals(cardUsedByUser, 0);
        User user2 = userService.findUserById(2).get();
        Integer winsByUser = achievementsService.winsByUser(user2);
        assertEquals(winsByUser, 1);
        Integer gamesPlayedByUser = achievementsService.gamesPlayedByUser(user1);
        assertEquals(gamesPlayedByUser, 1);

    }

    @Disabled
    @Test
    public void testScoresByUserAndCheckAchievement(){
        User user1 = userService.findUserById(1).get();
        Map<MetricType, Integer> map = achievementsService.scoresByUser(user1);
        Map<MetricType, Integer> mapExpected = new HashMap<>();
        mapExpected.put(MetricType.gamesPlayed, 1);
        mapExpected.put(MetricType.wins, 0);
        mapExpected.put(MetricType.cardsUsed, 0);
        assertEquals(map, mapExpected);

        Game newGame = new Game();
        newGame.setCreator(user1);
        newGame.setEndTime(LocalDateTime.now());
        newGame.setId(5);
        newGame.setMaxNumberOfPlayers(2);
        newGame.setName("Juego inventado");
        newGame.setWinner(user1.getUsername());
        newGame.setTurn(1);
        newGame.setStartTime(LocalDateTime.of(2021, 12, 26, 16, 15, 24));
        gameService.saveGame(newGame);
        assertEquals(newGame.isFinished(), true);

        achievementsService.checkAchievements(user1);
        
        Map<MetricType, Integer> map2 = achievementsService.scoresByUser(user1);
        Map<MetricType, Integer> mapExpected2 = new HashMap<>();
        mapExpected2.put(MetricType.gamesPlayed, 1);
        mapExpected2.put(MetricType.wins, 1);
        mapExpected2.put(MetricType.cardsUsed, 0);
        //check no añade la win al usuario
        assertEquals(map2, mapExpected2);
    }

 

}
