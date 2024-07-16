package com.example.SchedulEx.controller;

import com.example.SchedulEx.controllers.UserController;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.repositories.UserRepository;
import com.example.SchedulEx.helpers.PasswordHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private MockHttpSession session;

    @BeforeEach
    public void setup() throws Exception {
        session = new MockHttpSession();
        User user = new User("admin@example.com", "password", "Admin", "User", "ADMIN");
        session.setAttribute("user", user);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user = new User("john@example.com", "password", "John", "Doe", "USER");

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/user/all").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("allUsers"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("current"));
    }

    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testAddUser() throws Exception {
        mockMvc.perform(post("/user/add")
                .param("email", "newuser@example.com")
                .param("password", "password")
                .param("firstname", "New")
                .param("lastname", "User")
                .param("accesslevel", "USER")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:../action-panel"));

        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User("existinguser@example.com", "password", "Existing", "User", "USER");
        user.setUid(1);

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/user/update")
                .param("id", "1")
                .param("email", "updateduser@example.com")
                .param("password", "newpassword")
                .param("firstname", "Updated")
                .param("lastname", "User")
                .param("accesslevel", "ADMIN")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:allUsers"));

        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    public void testLoginUser() throws Exception {
        User user = new User("loginuser@example.com", "password", "Login", "User", "USER");

        Mockito.when(userRepository.findByEmail("loginuser@example.com")).thenReturn(Optional.of(user));
        Mockito.when(PasswordHelper.verifyPassword("password", user.getPassword(), user.getSalt())).thenReturn(true);

        mockMvc.perform(post("/user/login")
                .param("email", "loginuser@example.com")
                .param("password", "password")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:../action-panel"));
    }

    @Test
    public void testLoginError() throws Exception {
        mockMvc.perform(get("/user/login/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/user/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }
}
