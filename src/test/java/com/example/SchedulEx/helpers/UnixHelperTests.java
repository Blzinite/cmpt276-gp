package com.example.SchedulEx.helpers;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnixHelperTests {

    @Test
    public void testParseDate() {
        Long timestamp = UnixHelper.parseDate("2023-05-01", "12:00:00");
        assertEquals(1682942400000L, timestamp);
    }

    @Test
    public void testParseMomentMap() {
        Map<String, String> result = UnixHelper.parseMomentMap(1682942400000L);
        assertEquals("2023-05-01", result.get("date"));
        assertEquals("12:00:00", result.get("time"));
    }

    @Test
    public void testParseMomentJSON() {
        JSONObject result = UnixHelper.parseMomentJSON(1682942400000L);
        assertEquals("2023-05-01", result.get("date"));
        assertEquals("12:00:00", result.get("time"));
    }

    @Test
    public void testParseDateFromLong() {
        String result = UnixHelper.parseDate(1682942400000L);
        assertEquals("2023-05-01", result);
    }

    @Test
    public void testParseTimeFromLong() {
        String result = UnixHelper.parseTime(1682942400000L);
        assertEquals("12:00:00", result);
    }
}