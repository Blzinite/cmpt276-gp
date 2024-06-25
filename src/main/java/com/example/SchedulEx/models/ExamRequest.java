package com.example.SchedulEx.models;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "ExamRequests")
public class ExamRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String courseName;
    private int numberOfRooms;
    private int numberOfInvigilators;
    private Duration duration;
    private LocalDateTime firstDate;
    private LocalDateTime secondDate;
    private LocalDateTime thirdDate;

    public ExamRequest() {}
    public ExamRequest(String courseName, int numberOfRooms, Duration duration,
                       LocalDateTime firstDate, LocalDateTime secondDate, LocalDateTime thirdDate) {
        this.courseName = courseName;
        this.numberOfRooms = numberOfRooms;
        this.duration = duration;
        this.firstDate = firstDate;
        this.secondDate = secondDate;
        this.thirdDate = thirdDate;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public int getNumberOfRooms() {
        return numberOfRooms;
    }
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
    public int getNumberOfInvigilators() {
        return numberOfInvigilators;
    }
    public void setNumberOfInvigilators(int numberOfInvigilators) {
        this.numberOfInvigilators = numberOfInvigilators;
    }
    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public LocalDateTime getFirstDate() {
        return firstDate;
    }
    public void setFirstDate(LocalDateTime firstDate) {
        this.firstDate = firstDate;
    }
    public LocalDateTime getSecondDate() {
        return secondDate;
    }
    public void setSecondDate(LocalDateTime secondDate) {
        this.secondDate = secondDate;
    }
    public LocalDateTime getThirdDate() {
        return thirdDate;
    }
    public void setThirdDate(LocalDateTime thirdDate) {
        this.thirdDate = thirdDate;
    }
}
