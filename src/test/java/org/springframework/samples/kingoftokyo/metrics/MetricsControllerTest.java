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
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

    @Disabled("En proceso")
    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testRankingControllerOk() throws Exception {
        MetricType metric = MetricType.gamesPlayed;
        PageRequest pageable = PageRequest.of(0, 10);
        //Page<MetricData> pages = new Page<MetricData>();
        Mockito.when(metricService.rankingByMetricType(metric, 0, 10)).thenReturn(/*new Page<MetricData>()*/null);
        mockMvc.perform(get("/statistics/ranking"))
                .andExpect(status().isOk())
                .andExpect(view().name("modules/statistics/metrics/ranking"))
                .andExpect(model().attributeExists("metrics"))
                .andExpect(model().attributeExists("metrics"))
                .andExpect(model().attributeExists("totalElements"))
                .andExpect(model().attributeExists("number"))
                .andExpect(model().attributeExists("rows"))
                .andExpect(model().attributeExists("size"));

    }
    @Disabled("En proceso")
    @WithMockUser(value = "spring", authorities = { "user" })
    @Test
    void testStatisticsControllerOk() throws Exception {
        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("modaMonstername"))
                .andExpect(model().attributeExists("modaMonstericon"))
                .andExpect(model().attributeExists("nomodaMonstername"))
                .andExpect(model().attributeExists("nomodaMonstericon"));
    }


    
}
