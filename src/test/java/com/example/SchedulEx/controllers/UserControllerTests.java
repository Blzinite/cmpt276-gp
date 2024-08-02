package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.AccessLevel;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.repositories.UserRepository;
import com.example.SchedulEx.services.CourseService;
import com.example.SchedulEx.helpers.PasswordHelper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private UserRepository userRepo;

    @Mock
    private CourseService courseService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLogin() {
        String result = userController.getLogin(model);
        assertEquals("login", result);
    }

    @Test
    public void testGetUser() {
        User user = new User();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        String result = userController.getUser(1, model);

        verify(model).addAttribute("user", user);
        assertEquals("viewUser", result);
    }

    @Test
    public void testGetSelf() {
        User user = new User();
        when(session.getAttribute("user")).thenReturn(user);

        String result = userController.getSelf(model, session);

        verify(model).addAttribute("user", user);
        assertEquals("viewSelf", result);
    }

    @Test
    public void testAddUser() throws Exception {
        User admin = new User();
        admin.setAccessLevel(AccessLevel.ADMIN);
        when(session.getAttribute("user")).thenReturn(admin);

        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "password");
        params.put("firstname", "Test");
        params.put("lastname", "User");
        params.put("accesslevel", "INSTRUCTOR");

        String result = userController.addUser(params, model, session, response);

        verify(userRepo).save(any(User.class));
        assertEquals("redirect:../action-panel", result);
    }

    @Test
    public void testUpdateSelf() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");
        when(session.getAttribute("user")).thenReturn(user);
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("firstname", "NewFirst");
        params.put("lastname", "NewLast");

        String result = userController.updateSelf(params, model, session, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("redirect:../action-panel", result);
        assertEquals("NewFirst", user.getFirstName());
        assertEquals("NewLast", user.getLastName());
    }


    @Test
    public void testLoginUser() throws Exception {
        User user = new User("test@test.com", "hashedPassword", "Test", "User", "REGULAR");
        user.setNewUser(true); // Set the user as a new user
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        
        try (MockedStatic<PasswordHelper> mockedPasswordHelper = mockStatic(PasswordHelper.class)) {
            mockedPasswordHelper.when(() -> PasswordHelper.verifyPassword(anyString(), anyString(), any()))
                                .thenReturn(true);

            Map<String, String> params = new HashMap<>();
            params.put("email", "test@test.com");
            params.put("password", "password");

            String result = userController.loginUser(params, model, session, response);

            verify(session).setAttribute("user", user);
            verify(response).setStatus(HttpServletResponse.SC_OK);
            assertEquals("redirect:newPwd", result);
        }
    }

    @Test
    public void testLoginReturningUser() throws Exception {
        User user = new User("test@test.com", "hashedPassword", "Test", "User", "REGULAR");
        user.setNewUser(false); // Set the user as a returning user
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        
        try (MockedStatic<PasswordHelper> mockedPasswordHelper = mockStatic(PasswordHelper.class)) {
            mockedPasswordHelper.when(() -> PasswordHelper.verifyPassword(anyString(), anyString(), any()))
                                .thenReturn(true);

            Map<String, String> params = new HashMap<>();
            params.put("email", "test@test.com");
            params.put("password", "password");

            String result = userController.loginUser(params, model, session, response);

            verify(session).setAttribute("user", user);
            verify(response).setStatus(HttpServletResponse.SC_OK);
            assertEquals("redirect:../action-panel", result);
        }
    }

    @Test
    public void testLogout() {
        String result = userController.logout(session);

        verify(session).removeAttribute("user");
        verify(session).invalidate();
        assertEquals("redirect:/login", result);
    }

    @Test
    public void testGetActionPanel() {
        when(courseService.getActionPanel(model, session)).thenReturn("actionPanelView");

        String result = userController.getActionPanel(model, session);

        verify(courseService).getActionPanel(model, session);
        assertEquals("actionPanelView", result);
    }
}
