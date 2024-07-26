package com.example.SchedulEx.models;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTests {
    @Test
    public void testUpdateCourse(){
        Course course = new Course();
        Map<String, String> params = Map.of("examDate-1", "2020-01-01",
                "examDate-2", "2020-01-02",
                "examDate-3", "2020-01-03",
                "startTime-1", "13:00:00",
                "startTime-2", "12:00:00",
                "startTime-3", "12:00:00",
                "enrollment", "0",
                "duration", "2");
        course.updateCourse(params);
        assertEquals(2, course.getDuration());
        assertEquals(0, course.getEnrollment());
        assertEquals(RequestStatus.PENDING, course.getRequestStatus());
        assertEquals(1577883600000L, course.getDate(1));
        assertEquals(1577966400000L, course.getDate(2));
        assertEquals(1578052800000L, course.getDate(3));
    }

    @Test
    public void testOverlaps(){
        Course courseOne = new Course();
        Map<String, String> paramsOne = Map.of("examDate-1", "2020-01-01",
                "examDate-2", "2020-01-02",
                "examDate-3", "2020-01-03",
                "startTime-1", "12:00:00",
                "startTime-2", "12:00:00",
                "startTime-3", "12:00:00",
                "enrollment", "0",
                "duration", "2");
        courseOne.updateCourse(paramsOne);

        Course courseTwo = new Course();
        Map<String, String> paramsTwo = Map.of("examDate-1", "2020-01-01",
                "examDate-2", "2020-01-02",
                "examDate-3", "2020-01-03",
                "startTime-1", "13:00:00",
                "startTime-2", "13:00:00",
                "startTime-3", "13:00:00",
                "enrollment", "0",
                "duration", "2");
        courseTwo.updateCourse(paramsTwo);

        courseTwo.setRequestStatus(RequestStatus.ACCEPTED_TIME_ONE);
        assertTrue(courseOne.overlaps(courseTwo, 1));
        assertFalse(courseOne.overlaps(courseTwo, 2));
        assertFalse(courseOne.overlaps(courseTwo, 3));

        courseTwo.setRequestStatus(RequestStatus.ACCEPTED_TIME_TWO);
        assertFalse(courseOne.overlaps(courseTwo, 1));
        assertTrue(courseOne.overlaps(courseTwo, 2));
        assertFalse(courseOne.overlaps(courseTwo, 3));

        courseTwo.setRequestStatus(RequestStatus.ACCEPTED_TIME_THREE);
        assertFalse(courseOne.overlaps(courseTwo, 1));
        assertFalse(courseOne.overlaps(courseTwo, 2));
        assertTrue(courseOne.overlaps(courseTwo, 3));
    }
}