package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.AccessLevel;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CalendarControllerTests {

    @Mock
    private CourseService courseService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CalendarController calendarController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCalendarMonth() {
        String result = calendarController.getCalendarMonth(model, session);
        
        verify(model, times(4)).addAttribute(anyString(), any());
        assertEquals("calendarMonth", result);
    }

    @Test
    public void testPrevMonth() {
        Map<String, String> params = new HashMap<>();
        String result = calendarController.prevMonth(params, model, session);
        
        verify(model, times(4)).addAttribute(anyString(), any());
        assertEquals("calendarMonth", result);
    }

    @Test
    public void testUpdateCalendar() {
        Map<String, String> params = new HashMap<>();
        User user = new User();
        user.setAccessLevel(AccessLevel.INSTRUCTOR);
        when(session.getAttribute("user")).thenReturn(user);

        String result = calendarController.updateCalendar(params, model, session);
        
        verify(model, times(5)).addAttribute(anyString(), any());
        assertEquals("calendarMonth", result);
    }
}