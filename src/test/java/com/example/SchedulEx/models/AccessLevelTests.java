package com.example.SchedulEx.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccessLevelTests {

    @Test
    public void testParse() {
        assertEquals(AccessLevel.ADMIN, AccessLevel.parse("admin"));
        assertEquals(AccessLevel.INSTRUCTOR, AccessLevel.parse("instructor"));
        assertEquals(AccessLevel.INVIGILATOR, AccessLevel.parse("invigilator"));
        assertThrows(IllegalStateException.class, () -> AccessLevel.parse("invalid"));
    }

    @Test
    public void testToString() {
        assertEquals("admin", AccessLevel.ADMIN.toString());
        assertEquals("instructor", AccessLevel.INSTRUCTOR.toString());
        assertEquals("invigilator", AccessLevel.INVIGILATOR.toString());
    }
}