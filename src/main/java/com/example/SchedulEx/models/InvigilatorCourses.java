package com.example.SchedulEx.models;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="InvigilatorCourses")
public class InvigilatorCourses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private User invigilator;
    private String courseIds; //CSV string course IDs

    public InvigilatorCourses() {this.courseIds = "";}
    public InvigilatorCourses(User invigilator){
        this.invigilator = invigilator;
        this.courseIds = "";
    }
    public User getInvigilator() {
        return invigilator;
    }
    public void setInvigilator(User invigilator) {
        this.invigilator = invigilator;
    }
    public List<Integer> getCourseIds() {
        String[] tmp = courseIds.split(",");
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
}