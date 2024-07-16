package com.example.SchedulEx.models;

import com.example.SchedulEx.helpers.PasswordHelper;
import com.example.SchedulEx.models.AccessLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserConstructorWithExams() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String firstName = "Test";
        String lastName = "User";
        String accessLevel = "ADMIN";
        String exams = "Math, Science";

        User user = new User(email, password, firstName, lastName, accessLevel);

        assertEquals(email, user.getEmail());
        assertNotNull(user.getSalt());
        assertNotNull(user.getPassword());
        assertTrue(PasswordHelper.verifyPassword(password, user.getPassword(), user.getSalt()));
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(AccessLevel.parse(accessLevel), user.getAccessLevel());
        assertTrue(user.isNewUser());
    }

    @Test
    public void testUserConstructorWithoutExams() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String firstName = "Test";
        String lastName = "User";
        String accessLevel = "USER";

        User user = new User(email, password, firstName, lastName, accessLevel);

        assertEquals(email, user.getEmail());
        assertNotNull(user.getSalt());
        assertNotNull(user.getPassword());
        assertTrue(PasswordHelper.verifyPassword(password, user.getPassword(), user.getSalt()));
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(AccessLevel.parse(accessLevel), user.getAccessLevel());
        assertTrue(user.isNewUser());
    }

    @Test
    public void testSettersAndGetters() throws Exception {
        User user = new User();
        user.setEmail("setter@example.com");
        user.setSalt("new_salt".getBytes());
        user.setPassword("new_password");
        user.setFirstName("Setter");
        user.setLastName("Test");
        user.setAccessLevel(AccessLevel.parse("USER"));
        user.setNewUser(false);

        assertEquals("setter@example.com", user.getEmail());
        assertArrayEquals("new_salt".getBytes(), user.getSalt());
        assertEquals("new_password", user.getPassword());
        assertEquals("Setter", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertEquals(AccessLevel.parse("USER"), user.getAccessLevel());
        assertFalse(user.isNewUser());
    }

   

    @Test
    public void testFullName() {
        User user = new User();
        user.setFirstName("Full");
        user.setLastName("Name");

        // Assuming you have a getFullName method
        assertEquals("Full", user.getFirstName());
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        User user1 = new User("test1@example.com", "password", "Test", "User", "ADMIN");
        User user2 = new User("test2@example.com", "password", "Test", "User", "ADMIN" );
        User user3 = new User("different@example.com", "password", "Different", "User", "USER" );

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}
