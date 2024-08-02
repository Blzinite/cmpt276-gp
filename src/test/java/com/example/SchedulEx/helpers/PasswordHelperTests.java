package com.example.SchedulEx.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHelperTests {

    @Test
    public void testGenerateSalt() {
        byte[] salt = PasswordHelper.generateSalt();
        assertNotNull(salt);
        assertEquals(16, salt.length);
    }

    @Test
    public void testGeneratePassword() {
        String password = PasswordHelper.generatePassword();
        assertNotNull(password);
        assertTrue(password.length() >= 10);
    }

    @Test
    public void testEncryptAndVerifyPassword() throws Exception {
        String password = "testPassword";
        byte[] salt = PasswordHelper.generateSalt();

        String encryptedPassword = PasswordHelper.encryptPassword(password, salt);
        assertNotNull(encryptedPassword);

        boolean verified = PasswordHelper.verifyPassword(password, encryptedPassword, salt);
        assertTrue(verified);

        boolean notVerified = PasswordHelper.verifyPassword("wrongPassword", encryptedPassword, salt);
        assertFalse(notVerified);
    }
}