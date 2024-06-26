package com.example.SchedulEx.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ExamRequests")
public class ExamRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String courseName;
    private int numberOfRooms;
    private int numberOfInvigilators;
    private Long duration; //duration in ms
    private Long firstDate; //unix timestamp
    private Long secondDate; //unix timestamp
    private Long thirdDate; //unix timestamp

    public ExamRequest() {}
    public ExamRequest(String courseName, int numberOfRooms, Long duration,
                       Long firstDate, Long secondDate, Long thirdDate) {
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
    public Long getDuration() {
        return duration;
    }
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public Long getFirstDate() {
        return firstDate;
    }
    public void setFirstDate(Long firstDate) {
        this.firstDate = firstDate;
    }
    public Long getSecondDate() {
        return secondDate;
    }
    public void setSecondDate(Long secondDate) {
        this.secondDate = secondDate;
    }
    public Long getThirdDate() {
        return thirdDate;
    }
    public void setThirdDate(Long thirdDate) {
        this.thirdDate = thirdDate;
    }
}
