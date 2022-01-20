package org.springframework.samples.kingoftokyo.achievements;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.configuration.CurrentUserController;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.Achievement;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementController;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;


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


    /**
     * Test positivo, ya que he iniciado sesión como admin,
     * puedo acceder a la pagina de achievements
     * @throws Exception
     */

    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    void testAchievementControllerOk() throws Exception {
        mockMvc.perform(get("/achievements"))
                .andExpect(status().isOk());

    }

    /**
     * Test negativo, ya que no he iniciado sesión o no soy admin,
     *  no puedo acceder a la pagina de achievements
     * @throws Exception
     */ 

    @Test
    void testAchievementControllerNoInicio() throws Exception {
        mockMvc.perform(get("/achievements"))
                .andExpect(status().is4xxClientError());

    }

    /**
     * Test positivo, dado que soy admin, puedo acceder al formulario de creación
     * de logros
     * @throws Exception
     */

    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    void testAchievementCreateControllerOk() throws Exception {
        mockMvc.perform(get("/achievements/new"))
                .andExpect(status().isOk());

    }
 
    /**
     * Test negativo, dado que no he inciado sesión o no soy admin,
     * no puedo acceder al formulario de creación de logros
     * @throws Exception
     */

    @Test
    void testAchievementCreateControllerNotOk() throws Exception {
        mockMvc.perform(get("/achievements/new"))
                .andExpect(status().is4xxClientError());

    }

    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    void testAchievementCreateController() throws Exception {
        mockMvc.perform(post("/achievements/new")
                    .with(csrf())
                    .param("name", "name")
                    .param("description", "description")
                    .param("rewardPoints", "20")
                    .param("goal", "1")
                    .param("metric", "wins"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/achievements/"));

    }

    @WithMockUser(value = "spring", authorities = { "admin" })
    @Test
    void testAchievementCreateControllerWithErrors() throws Exception {
        mockMvc.perform(post("/achievements/new")
                    .with(csrf())
                    .param("name", "Name")
                    .param("description", "description")
                    .param("rewardPoints", "-20")
                    .param("goal", "1")
                    .param("metric", "wins"))
                .andExpect(status().isOk())
                .andExpect(view().name("modules/statistics/achievements/newAchievement"));

    }



}
