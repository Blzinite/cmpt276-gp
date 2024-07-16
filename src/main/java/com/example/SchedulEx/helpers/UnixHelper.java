package com.example.SchedulEx.helpers;

import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


//Helper functions for parsing Unix timestamps
public class UnixHelper {
    public static Long parseDate(String date, String time) {
        LocalDateTime out = LocalDateTime.parse(date + "T" + time);
        return out.toInstant(ZoneOffset.UTC).toEpochMilli() / 1000;
    }
    public static Map<String, String> parseMomentMap(Long moment){
        Map<String, String> map = new HashMap<>();
        LocalDateTime tmp = LocalDateTime.ofEpochSecond(moment, 0, ZoneOffset.UTC);
        map.put("date", tmp.format(DateTimeFormatter.ISO_LOCAL_DATE));
        map.put("time", tmp.format(DateTimeFormatter.ISO_LOCAL_TIME));
        return map;
    }
    public static JSONObject parseMomentJSON(Long moment){
        JSONObject map = new JSONObject();
        LocalDateTime tmp = LocalDateTime.ofEpochSecond(moment, 0, ZoneOffset.UTC);
        map.put("date", tmp.format(DateTimeFormatter.ISO_LOCAL_DATE));
        map.put("time", tmp.format(DateTimeFormatter.ISO_LOCAL_TIME));
        return map;
    }
    public static String parseDate(Long moment){
        LocalDateTime tmp = LocalDateTime.ofEpochSecond(moment, 0, ZoneOffset.UTC);
        return tmp.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    public static String parseTime(Long moment){
        LocalDateTime tmp = LocalDateTime.ofEpochSecond(moment, 0, ZoneOffset.UTC);
        return tmp.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
}