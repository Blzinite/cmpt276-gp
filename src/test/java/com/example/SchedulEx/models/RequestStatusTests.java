package com.example.SchedulEx.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequestStatusTests {

    @Test
    public void testIsAccepted() {
        assertTrue(RequestStatus.isAccepted(RequestStatus.ACCEPTED_TIME_ONE));
        assertTrue(RequestStatus.isAccepted(RequestStatus.ACCEPTED_TIME_TWO));
        assertTrue(RequestStatus.isAccepted(RequestStatus.ACCEPTED_TIME_THREE));
        assertTrue(RequestStatus.isAccepted(RequestStatus.ACCEPTED_CUSTOM_TIME));
        assertFalse(RequestStatus.isAccepted(RequestStatus.PENDING));
        assertFalse(RequestStatus.isAccepted(RequestStatus.REJECTED));
    }
}