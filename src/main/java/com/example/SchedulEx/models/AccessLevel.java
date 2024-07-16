package com.example.SchedulEx.models;

public enum AccessLevel
{
    ADMIN,
    INSTRUCTOR,
    INVIGILATOR;

    public static AccessLevel parse(String accessLevel) {
        return switch (accessLevel.toLowerCase()) {
            case "admin" -> AccessLevel.ADMIN;
            case "instructor" -> AccessLevel.INSTRUCTOR;
            case "invigilator" -> AccessLevel.INVIGILATOR;
            default -> throw new IllegalStateException("Unexpected value: " + accessLevel.toLowerCase());
        };
    }

    @Override
    public String toString()
    {
        return switch (this)
        {
            case ADMIN -> "admin";
            case INSTRUCTOR -> "instructor";
            case INVIGILATOR -> "invigilator";
        };
    }
}