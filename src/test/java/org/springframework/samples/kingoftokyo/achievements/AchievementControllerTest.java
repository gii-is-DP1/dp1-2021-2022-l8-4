package org.springframework.samples.kingoftokyo.achievements;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.configuration.CurrentUserController;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementController;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;


/**
 * @author Rosa Molina
 */

@WebMvcTest(value = AchievementController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
            excludeAutoConfiguration = SecurityConfiguration.class)

public class AchievementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AchievementService achievementService;

    @MockBean
    private CurrentUserController currentUserController;

    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    public void testAchievementControllerOk() throws Exception {
        mockMvc.perform(get("/achievements"))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    public void testAchievementCreateControllerOk() throws Exception {
        mockMvc.perform(get("/achievements/new"))
                .andExpect(status().isOk());

    }

    @Disabled
    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    public void testAchievementCreateControllerError() throws Exception {
        mockMvc.perform(post("/product/create")
                    .with(csrf())
                    .param("name", "")
                    .param("description", "")
                    .param("rewardPoints", "-20")
                    .param("metric", "1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("achievements"));

    }


}
