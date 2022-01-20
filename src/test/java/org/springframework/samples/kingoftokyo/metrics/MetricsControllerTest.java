package org.springframework.samples.kingoftokyo.metrics;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricController;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.samples.kingoftokyo.player.Monster;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


/**
 * @author Noelia López Durán
 */
@WebMvcTest(value = MetricController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
            excludeAutoConfiguration = SecurityConfiguration.class)
            
            
class MetricsControllerTest {
    
    @MockBean
    private UserService userService;

    @MockBean
    private MetricService metricService;

    @MockBean
    private AchievementService achievementService;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testRankingControllerOk() throws Exception {
        MetricType metric = MetricType.gamesPlayed;
        List<MetricData> a = new ArrayList<>();
        Page<MetricData> pages = new PageImpl<>(a);
        Mockito.when(metricService.rankingByMetricType(metric, 0, 10)).thenReturn(pages);
        mockMvc.perform(get("/statistics/ranking"))
                .andExpect(status().isOk())
                .andExpect(view().name("modules/statistics/metrics/ranking"))
                .andExpect(model().attributeExists("metrics"))
                .andExpect(model().attributeExists("actualMetric"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalElements"))
                .andExpect(model().attributeExists("number"))
                .andExpect(model().attributeExists("rows"))
                .andExpect(model().attributeExists("size"));
    }
    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testStatisticsControllerOk() throws Exception {
        List<MetricData> lista = new ArrayList<>();
        User user =  new User();
        Mockito.when(userService.authenticatedUser()).thenReturn(user);
        Mockito.when(metricService.findTotalGamesApp()).thenReturn(5);
        Mockito.when(metricService.findTimeGames()).thenReturn(1);
        Mockito.when(metricService.findMonsterModa()).thenReturn(Monster.gigaZaur);
        Mockito.when(metricService.findMonsterNoModa()).thenReturn(Monster.alien);
        Mockito.when(metricService.winsRankingStatistic()).thenReturn(lista);
        Mockito.when(metricService.scoresRankingStatistic()).thenReturn(lista);
        Mockito.when(metricService.findTotalWinsGamesCurrentUser(anyString())).thenReturn(1);
        Mockito.when(metricService.findTotalGamesCurrentUser(any(User.class))).thenReturn(3);
        Mockito.when(metricService.findTimeGamesforUser(any(User.class))).thenReturn(1);
        Mockito.when(metricService.findTurnsTokyo()).thenReturn(lista);

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("modules/statistics/metrics/statistics"))
                .andExpect(model().attributeExists("modaMonstername"))
                .andExpect(model().attributeExists("modaMonstericon"))
                .andExpect(model().attributeExists("nomodaMonstername"))
                .andExpect(model().attributeExists("nomodaMonstericon"))
                .andExpect(model().attributeExists("totalGames"))
                .andExpect(model().attributeExists("mediumGameTime"))
                .andExpect(model().attributeExists("listWinsRanking"))
                .andExpect(model().attributeExists("listScoresRanking"))
                .andExpect(model().attributeExists("winsCurrent"))
                .andExpect(model().attributeExists("gamesCurrent"))
                .andExpect(model().attributeExists("gamesTimeCurrent"))
                .andExpect(model().attributeExists("listUsersTurns"));
    }

    @Test
    void testShouldNotAccessRankingBecauseNotLogged() throws Exception {
        mockMvc.perform(get("/statistics/ranking"))
        .andExpect(status().is4xxClientError());
    }

    @Test
    void testShouldNotAccessStatisticsBecauseNotLogged() throws Exception {
        mockMvc.perform(get("/statistics"))
        .andExpect(status().is4xxClientError());
    }
}
