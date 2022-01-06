package org.springframework.samples.petclinic.achievements;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.internal.ExactComparisonCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.modules.statistics.achievement.Achievement;
import org.springframework.samples.petclinic.modules.statistics.achievement.AchievementController;
import org.springframework.samples.petclinic.modules.statistics.achievement.AchievementService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.stereotype.Service;


/**
 * @author Rosa Molina
 */


@WebMvcTest(value = AchievementController.class)

public class AchievementControllerTest {
    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private AchievementService achievementService;

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Disabled
    @Test
    public void testAchievementCreationControllerOk() throws Exception {
        mockMvc.perform(get("/achievements"))
        .andExpect(status().isOk());

    }
}
