package com.example.SchedulEx.models;

import com.example.SchedulEx.helpers.PasswordHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private User user;

    @BeforeEach
    public void setup() throws Exception {
        user = new User("test@example.com", "password", "John", "Doe", "INSTRUCTOR");
    }

    @Test
    public void testUserCreation() {
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(AccessLevel.INSTRUCTOR, user.getAccessLevel());
        assertTrue(user.isNewUser());
    }

    @Test
    public void testSetPassword() throws Exception {
        String newPassword = "newPassword";
        user.setPassword(newPassword);
        assertTrue(PasswordHelper.verifyPassword(newPassword, user.getPassword(), user.getSalt()));
    }

    @Test
    public void testAddAndRemoveCourse() {
        Course course = new Course();
        user.addCourse(course);
        assertEquals(1, user.getCourses().size());

        user.removeCourse(course);
        assertEquals(0, user.getCourses().size());
    }

    @Test
    public void testToString() {
        assertEquals("John Doe (test@example.com)", user.toString());
    }
}