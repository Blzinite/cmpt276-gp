package com.example.SchedulEx.models;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="InvigilatorData")
public class InvigilatorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.REMOVE)
    private User invigilator;
    private String courseIds; //CSV string course IDs
    private String acceptedCourses; //CSV string containing course IDs
    private String unavailableDates; //CSV dates formatted as unix timestamps

    public InvigilatorData() {this.courseIds = ""; this.acceptedCourses = ""; this.unavailableDates = "";}
    public InvigilatorData(User invigilator){
        this.invigilator = invigilator;
        this.courseIds = "";
        this.acceptedCourses = "";
        this.unavailableDates = "";
    }
    public User getInvigilator() {
        return invigilator;
    }
    public void setInvigilator(User invigilator) {
        this.invigilator = invigilator;
    }
    public List<Integer> getCourseIds() {
        String[] tmp = courseIds.split(",");
        System.out.print("OVERHERE!");
        System.out.println(courseIds);
        List<Integer> courseIdList = new ArrayList<>();
        for(String str : tmp){
            courseIdList.add(Integer.parseInt(str));
        }
        return courseIdList;
    }
    public void setCourseIds(List<Integer> courseIds) {
        this.courseIds = StringUtils.join(courseIds.toArray(), ",");
    }
    public void addCourse(int courseId) {
        this.courseIds = this.courseIds + "," + courseId;
    }
    public void removeCourse(Integer courseId) {
        String[] tmp = this.courseIds.split(",");
        List<String> tmpList = new ArrayList<>(Arrays.stream(tmp).toList());
        tmpList.remove(courseId.toString());
        this.courseIds = StringUtils.join(tmpList.toArray(), ",");
    }
    public List<Long> getUnavailableDates() {
        String[] tmp = unavailableDates.split(",");
        List<Long> unavailableDateList = new ArrayList<>();
        for(String str : tmp){
            unavailableDateList.add(Long.parseLong(str));
        }
        return unavailableDateList;
    }
    public void addUnavailableDate(long unavailableDate) {
        this.unavailableDates = this.unavailableDates + "," + unavailableDate;
    }
    public void removeUnavailableDate(Long unavailableDate) {
        String[] tmp = this.unavailableDates.split(",");
        List<String> tmpList = new ArrayList<>(Arrays.stream(tmp).toList());
        tmpList.remove(unavailableDate.toString());
        this.unavailableDates = StringUtils.join(tmpList.toArray(), ",");
    }
    public void acceptAssignment(int courseId){
        this.acceptedCourses = this.acceptedCourses + "," + courseId;
    }
    public void removeAcceptedAssignment(Integer courseId){
        String[] tmp = this.acceptedCourses.split(",");
        List<String> tmpList = new ArrayList<>(Arrays.stream(tmp).toList());
        tmpList.remove(courseId.toString());
        this.acceptedCourses = StringUtils.join(tmpList.toArray(), ",");
    }
}