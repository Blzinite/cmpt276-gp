package com.example.SchedulEx.models;

public class AccessLevel {
    public static final int ADMIN = 601;
    public static final int PROFESSOR = 602;
    public static final int INVIGILATOR = 603;

    public static int parse(String accessLevel) {
        return switch (accessLevel) {
            case "admin" -> ADMIN;
            case "professor" -> PROFESSOR;
            case "invigilator" -> INVIGILATOR;
            default -> -999;
        };
    }
}