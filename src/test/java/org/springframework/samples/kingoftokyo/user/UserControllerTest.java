package org.springframework.samples.kingoftokyo.user;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.samples.kingoftokyo.configuration.CurrentUserController;
import org.springframework.samples.kingoftokyo.configuration.SecurityConfiguration;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Carlos Varela Soult
 */

@WebMvcTest(value = UserController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
            excludeAutoConfiguration = SecurityConfiguration.class)

public class UserControllerTest {

    private static final Integer TEST_USER_ID = 17;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach()
    public void setup() {
        //Init MockMvc Object and build
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @MockBean
    private UserService userService;
    @MockBean
    private AchievementService achievementService;
    @MockBean
    private CurrentUserController currentUserController;

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testUsersList() throws Exception {
        List<User> listuser = new ArrayList<>();
        Page<User> pages = new PageImpl<User>(listuser);
        Mockito.when(userService.getPageOfUsers(anyInt())).thenReturn(pages);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalElements"))
                .andExpect(model().attributeExists("number"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("size"))
                .andExpect(view().name("users/usersList"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testCreateNewUser() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/createUsersForm"))
                .andExpect(model().attributeExists("user"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testPostNewUser() throws Exception {
        mockMvc.perform(post("/users/new")
                        .with(csrf())
                        .param("username", "hecker")
                        .param("email", "hecker@email.com")
                        .param("password", "h3ck3r"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testPostNewUserHasErrors() throws Exception {
        String notAnEmail = "hecker";
        mockMvc.perform(post("/users/new")
                        .with(csrf())
                        .param("username", "hecker")
                        .param("email", notAnEmail)
                        .param("password", "h3ck3r"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(view().name("users/createUsersForm"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testUpdateCurrentUser() throws Exception {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setEnabled(true);
        user.setMaxTurnsTokyo(0L);
        Mockito.when(userService.findUserById(TEST_USER_ID)).thenReturn(user);
        Mockito.when(userService.authenticatedUser()).thenReturn(user);
        Mockito.when(userService.isAdmin(TEST_USER_ID)).thenReturn(true);
        mockMvc.perform(get("/users/{userId}/edit", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("users/updateUsersForm"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testPostCurrentUserWithUpdatedData() throws Exception {
        String newPassword = "pass";
        String oldPassword = "old";

        User user = new User();
        user.setId(TEST_USER_ID);
        user.setVersion(2);
        user.setPassword(oldPassword);
        user.setMaxTurnsTokyo(0L);

        User userToUpdate = new User();
        BeanUtils.copyProperties(user, userToUpdate, "id");

        Mockito.when(userService.findUserById(TEST_USER_ID)).thenReturn(user);
        Mockito.when(userService.passwordCheckEdit(oldPassword, newPassword, userToUpdate))
                .thenReturn(userToUpdate);

        mockMvc.perform(post("/users/{userId}/edit", TEST_USER_ID)
                        .with(csrf())
                        .param("username", "2cabezas")
                        .param("email", "2cabezas@email.com")
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword)
                        .param("version", "2")
                        .param("maxTurns", "0L"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/updateUsersForm"));
    }


    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testPostCurrentUserWithUpdatedDataHasErrors() throws Exception {
        String newPassword = "";
        String oldPassword = "old";

        User user = new User();
        user.setId(TEST_USER_ID);
        user.setVersion(2);
        user.setPassword(oldPassword);
        user.setMaxTurnsTokyo(0L);

        User userToUpdate = new User();
        BeanUtils.copyProperties(user, userToUpdate, "id");

        Mockito.when(userService.findUserById(TEST_USER_ID)).thenReturn(user);
        Mockito.when(userService.passwordCheckEdit(oldPassword, newPassword, userToUpdate))
                .thenReturn(userToUpdate);
        
        mockMvc.perform(post("/users/{userId}/edit", TEST_USER_ID)
                        .with(csrf())
                        .param("username", "2cabezas")
                        .param("email", "2cabezas@email.com")
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword)
                        .param("version", "2")
                        .param("maxTurns", "0L"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/updateUsersForm"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testUsersProfile() throws Exception {
        mockMvc.perform(get("/users/profile/{userId}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("users/profile"));
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setId(TEST_USER_ID);
        Mockito.when(userService.findUserById(TEST_USER_ID)).thenReturn(user);

        mockMvc.perform(get("/users/delete/{userId}", TEST_USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users?page=1"));
    }
}