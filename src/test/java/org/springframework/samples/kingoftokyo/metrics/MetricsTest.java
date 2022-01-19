package org.springframework.samples.kingoftokyo.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.game.GameService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.samples.kingoftokyo.player.Monster;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

/**
/* @author Rosa Molina
/* @author Carlos Varela Soult
*/

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(SecurityConfiguration.class)
class MetricsTest {

    @Autowired
    private MetricService metricService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    
    @Test
    void shouldCreateMetric() {
        User user1 = userService.findUserById(1);        
        MetricData metric = new MetricData(user1, 10l);
        assertEquals(user1, metric.getUser());
        assertEquals(10l, metric.getScore());
    }

    @Test
    void shouldUpdateMetric() {
        User user1 = userService.findUserById(1);
        User user2 = userService.findUserById(2);
        MetricData metric = new MetricData(user1, 10l);
        metric.setScore(20l);
        metric.setUser(user2);
        assertEquals(user2, metric.getUser());
        assertEquals(20l, metric.getScore());
    }

    @Test
    void shouldGetNameOfMetricType(){
        MetricType metricType = MetricType.wins;
        assertEquals("Victorias", metricType.getName());
    }
 
    @Test
    void shouldFindTotalGames(){
        Integer totalGames = metricService.findTotalGamesApp();
        assertNotNull(totalGames);
    }

    @Test
    void shouldFindTotalGamesWinsByUser(){
        Integer totalGamesWins = metricService.findTotalWinsGamesCurrentUser("user2");
        assertEquals(1, totalGamesWins);
    }

    @Test
    void shouldFindTotalGamesByUser(){
        User user = userService.findUserById(1);
        Integer totalGames = metricService.findTotalGamesCurrentUser(user);
        assertEquals(1, totalGames);
    }

    @Test
    void shouldFindTimeByGames(){
        Integer timeGames = metricService.findTimeGames();
        assertNotNull(timeGames);
    }

    @Test
    void shouldFindTimeOfGamesByUser(){
        User user = userService.findUserById(1);
        Integer timeGames = metricService.findTimeGamesforUser(user);
        assertEquals(60, timeGames);
    }

    @Test
    void shouldFindMonsterModa(){
        Monster monster = metricService.findMonsterModa();
        assertEquals(Monster.mekaDracon, monster);
    }

    @Test
    void shouldFindMonsterNoModa(){
        Monster monster = metricService.findMonsterNoModa();
        assertEquals(Monster.gigaZaur, monster);
    }

    @Test
    void shouldFindRankingByMetricType(){
        MetricType wins = MetricType.wins;
        MetricType gamesPlayed = MetricType.gamesPlayed;
        MetricType turnsTokyo = MetricType.turnsTokyo;
        MetricType cardsUsed = MetricType.cardsUsed;

        Page<MetricData> pageWins = metricService.rankingByMetricType(wins, 0, 1);
        Page<MetricData> pageGamesPlayed = metricService.rankingByMetricType(gamesPlayed, 0, 1);
        Page<MetricData> pageTurnsTokyo = metricService.rankingByMetricType(turnsTokyo, 0, 1);
        //Page<MetricData> pageCardsUsed = metricService.rankingByMetricType(cardsUsed, 0, 1);

        User user2 = userService.findUserById(2);
        User user1 = userService.findUserById(1);

        MetricData userWins = new MetricData(user2, 1l);
        MetricData userGamesPlayed = new MetricData(user1, 1l);
        MetricData userTurnsTokyo = new MetricData(user1, 0l);
        //MetricData userCardsUsed = new MetricData(user1, 0l);

        assertEquals(userWins.getUser().getId(), pageWins.toList().get(0).getUser().getId());
        assertEquals(userWins.getScore(), pageWins.toList().get(0).getScore());

        assertEquals(userGamesPlayed.getUser().getId(), pageGamesPlayed.toList().get(0).getUser().getId());
        assertEquals(userGamesPlayed.getScore(), pageGamesPlayed.toList().get(0).getScore());

        assertEquals(userTurnsTokyo.getUser().getId(), pageTurnsTokyo.toList().get(0).getUser().getId());
        assertEquals(userTurnsTokyo.getScore(), pageTurnsTokyo.toList().get(0).getScore());

        //assertEquals(userCardsUsed.getUser().getId(), pageCardsUsed.toList().get(0).getUser().getId());
        //assertEquals(userCardsUsed.getScore(), pageCardsUsed.toList().get(0).getScore());
    }

    @Test
    void shouldFindTurnsTokyo(){
        User userTurns = metricService.findTurnsTokyo().get(0).getUser();
        Long turns = metricService.findTurnsTokyo().get(0).getScore();
        assertEquals(Integer.class, userTurns.getId().getClass()); //Everyone has 0 turns, so aleatory id
        assertEquals(0l, turns);
    }

    @Test
    void shouldFindScoresRanking(){
        List<MetricData> lista = metricService.scoresRankingStatistic();
        assertTrue(lista.isEmpty());
        //is empty because no-user has achievements yet
    }

    @Test
    void shouldFindScoresRankingStatistic(){
        List<MetricData> lista = metricService.scoresRankingStatistic();
        assertTrue(lista.isEmpty());
        //is empty because no-user has achievements yet
    }

    @Test
    void shouldFindWinsRankingStatistic(){
        User user = metricService.winsRankingStatistic().get(0).getUser();
        Long scores = metricService.winsRankingStatistic().get(0).getScore();
        assertEquals(2, user.getId());
        assertEquals(1l, scores);
    }

}
