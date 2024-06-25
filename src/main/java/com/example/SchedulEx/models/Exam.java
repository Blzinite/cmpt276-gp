package com.example.SchedulEx.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int examId;
    private Long requestId;
    private String assignedRooms; //list of rooms separated by comma
    private String invigilators; //list of invigilators separated by comma
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Exam() {}
    public Exam(int examId, Long requestId, String assignedRooms, String invigilators,
                LocalDateTime startTime, LocalDateTime endTime) {
        this.examId = examId;
        this.requestId = requestId;
        this.assignedRooms = assignedRooms;
        this.invigilators = invigilators;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public int getExamId() {
        return examId;
    }
    public void setExamId(int examId) {
        this.examId = examId;
    }
    public Long getRequestId() {
        return requestId;
    }
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    public String getAssignedRooms() {
        return assignedRooms;
    }
    public void setAssignedRooms(String assignedRooms) {
        this.assignedRooms = assignedRooms;
    }
    public String getInvigilators() {
        return invigilators;
    }
    public void setInvigilators(String invigilators) {
        this.invigilators = invigilators;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
