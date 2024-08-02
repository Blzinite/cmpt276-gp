package com.example.SchedulEx.services;

import com.example.SchedulEx.models.*;
import com.example.SchedulEx.repositories.CourseRepository;
import com.example.SchedulEx.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CourseServiceTests {

    private CourseService courseService;
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        courseRepository = mock(CourseRepository.class);
        courseService = new CourseService(userRepository, courseRepository);
        session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    @Test
    void testGetAllExcept() {
        Course course1 = new Course("TEST-101", 30, new User());
        Course course2 = new Course("TEST-102", 30, new User());
        when(courseRepository.findByCourseNameNot("TEST-101")).thenReturn(Arrays.asList(course2));

        JSONArray result = courseService.getAllExcept("TEST-101");

        assertNotNull(result);
        verify(courseRepository).findByCourseNameNot("TEST-101");
    }

    @Test
    void testCreateNewCourse() {
        User instructor = new User();
        when(session.getAttribute("user")).thenReturn(instructor);
        Map<String, String> params = new HashMap<>();
        params.put("department", "TEST");
        params.put("number", "101");
        params.put("enrollment", "30");

        courseService.createNewCourse(params, session);

        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void testGetUserCourses() {
        User instructor = new User();
        instructor.setAccessLevel(AccessLevel.INSTRUCTOR);
        when(session.getAttribute("user")).thenReturn(instructor);

        List<Course> result = courseService.getUserCourses(model, session);

        assertNotNull(result);
    }

    @Test
    void testGetOverlaps() {
        Course course = new Course("TEST-101", 30, new User());
        Course overlappingCourse = new Course("TEST-102", 30, new User());
        overlappingCourse.setRequestStatus(RequestStatus.ACCEPTED_TIME_ONE);
        
        List<Course> acceptedCourses = new ArrayList<>();
        acceptedCourses.add(overlappingCourse);
        
        when(courseRepository.findByRequestStatusBetween(RequestStatus.ACCEPTED_TIME_ONE, RequestStatus.ACCEPTED_CUSTOM_TIME))
                .thenReturn(acceptedCourses);

        Map<Integer, List<String>> result = courseService.getOverlaps(course);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testGetCourse() {
        Course course = new Course("TEST-101", 30, new User());
        when(courseRepository.findByCourseName("TEST-101")).thenReturn(Optional.of(course));

        Optional<Course> result = courseService.getCourse("TEST-101");

        assertTrue(result.isPresent());
        assertEquals("TEST-101", result.get().getCourseName());
    }

    @Test
    void testGetActionPanel() {
        User admin = new User();
        admin.setAccessLevel(AccessLevel.ADMIN);
        when(session.getAttribute("user")).thenReturn(admin);

        String result = courseService.getActionPanel(model, session);

        assertEquals("admin", result);
        verify(model, times(3)).addAttribute(anyString(), any());
    }

    @Test
    void testConfirmUserAccessLevel() {
        User instructor = new User();
        instructor.setAccessLevel(AccessLevel.INSTRUCTOR);
        when(session.getAttribute("user")).thenReturn(instructor);

        boolean result = courseService.confirmUserAccessLevel(AccessLevel.INSTRUCTOR, session);

        assertTrue(result);
    }

}