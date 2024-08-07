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
    private Long dateOne = 0L;
    private Long dateTwo = 0L;
    private Long dateThree = 0L;
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
        this.requestStatus = RequestStatus.PENDING;
    }

    public void setRequestStatus(int status){
        this.requestStatus = status;
    }

    public int getRequestStatus(){
        return this.requestStatus;
    }

    public void setInstructor(User instructor)
    {
        this.instructor = instructor;
    }

    public int getEnrollment()
    {
        return enrollment;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getInstructorID()
    {
        return instructor.getUid();
    }

    public String getCourseName(){
        return courseName;
    }

    public String getApprovedCourse() {
        return switch (requestStatus) {
            case RequestStatus.ACCEPTED_TIME_ONE ->
                    String.format("%s (%s@%s)", courseName, getExamDate(1), getStartTime(1));
            case RequestStatus.ACCEPTED_TIME_TWO ->
                    String.format("%s (%s@%s)", courseName, getExamDate(2), getStartTime(2));
            case RequestStatus.ACCEPTED_TIME_THREE, RequestStatus.ACCEPTED_CUSTOM_TIME ->
                    String.format("%s (%s@%s)", courseName, getExamDate(3), getStartTime(3));
            default -> courseName;
        };
    }

    public void removeInstructor()
    {
        instructor = null;
    }

    public int getCourseID()
    {
        return courseID;
    }

    public void setCustomTime(Long newTime){
        this.dateThree = newTime;
    }

    public Long getDate(int which){
        return switch (which){
            case 1 -> this.dateOne;
            case 2 -> this.dateTwo;
            case 3, 4 -> this.dateThree;
            default -> -999L;
        };
    }

    public String getExamDate(int which){
        return switch (which){
            case 1 -> UnixHelper.parseDate(dateOne);
            case 2 -> UnixHelper.parseDate(dateTwo);
            case 3 -> UnixHelper.parseDate(dateThree);
            default -> "Invalid Selection";
        };
    }

    public String getStartTime(int which){
        return switch (which){
            case 1 -> UnixHelper.parseTime(dateOne);
            case 2 -> UnixHelper.parseTime(dateTwo);
            case 3 -> UnixHelper.parseTime(dateThree);
            default -> "Invalid Selection";
        };
    }

    public JSONArray getDates(){
        JSONArray out = new JSONArray();
        Long[] dates = {this.dateOne, this.dateTwo, this.dateThree};
        for(Long date : dates){
            JSONObject tmp = new JSONObject();
            tmp.put("name", this.toString());
            tmp.put("date", UnixHelper.parseDate(date));
            tmp.put("start", UnixHelper.parseTime(date));
            tmp.put("duration", this.duration);
            out.add(tmp);
        }
        return out;
    }

    //other must be an accepted course
    public boolean overlaps(Course other, int which){
        Long thisStart = this.getDate(which);
        Long thisEnd = thisStart + (this.duration * 60 * 60 * 1000L);
        Long otherStart = other.getDate(other.getRequestStatus()-700); //this is jank, do not just pass any course for 'other'
        Long otherEnd = otherStart + (other.getDuration() * 60 * 60 * 1000L);
        return (thisEnd >= otherStart && thisStart <= otherEnd);
    }

    @Override
    public String toString(){
        String[] tmp = this.courseName.split("-");
        return tmp[0] + " " + tmp[1];
    }

    @Override
    public boolean equals(Object o)
    {
        Course other = (Course) o;
        return this.courseID == other.courseID;
    }
}
