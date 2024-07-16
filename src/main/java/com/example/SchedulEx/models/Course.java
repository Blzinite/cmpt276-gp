package com.example.SchedulEx.models;

import com.example.SchedulEx.helpers.UnixHelper;
import jakarta.persistence.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

@Entity
@Table(name="Courses")
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseID;
    private String courseName;
    private int enrollment = 0;
    private Long dateOne;
    private Long dateTwo;
    private Long dateThree;
    private int requestStatus;
    private int duration = 0;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User instructor;

    public Course()
    {

    }

    public Course(String name, int enrollment, User instructor)
    {
        this.enrollment = enrollment;
        this.courseName = name;
        this.instructor = instructor;
        this.requestStatus = -1;
    }

    public void updateCourse(Map<String,String> params){
        this.dateOne = UnixHelper.parseDate(params.get("examDate-1"), params.get("startTime-1"));
        this.dateTwo = UnixHelper.parseDate(params.get("examDate-2"), params.get("startTime-2"));
        this.dateThree = UnixHelper.parseDate(params.get("examDate-3"), params.get("startTime-3"));
        this.enrollment = Integer.parseInt(params.get("enrollment"));
        this.duration = Integer.parseInt(params.get("duration"));
    }

    public void SetInstructor(User instructor)
    {
        this.instructor = instructor;
    }

    public int GetEnrollment()
    {
        return enrollment;
    }

    public int GetDuration()
    {
        return duration;
    }

    public int GetInstructorID()
    {
        System.out.println(instructor.getUid());
        return instructor.getUid();
    }

    public String getCourseName(){
        return courseName;
    }

    public void RemoveInstructor()
    {
        instructor = null;
    }

    public int GetCourseID()
    {
        return courseID;
    }

    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("courseName", this.courseName);
        obj.put("enrollment", this.enrollment);
        obj.put("duration", this.duration);
        JSONArray arr = new JSONArray();
        arr.add(UnixHelper.parseMoment(this.dateOne));
        arr.add(UnixHelper.parseMoment(this.dateTwo));
        arr.add(UnixHelper.parseMoment(this.dateThree));
        obj.put("dates", arr);
        return obj;
    }

    @Override
    public boolean equals(Object o)
    {
        Course other = (Course) o;
        if(this.courseID == other.courseID)
        {
            return true;
        }
        return false;
    }
}
