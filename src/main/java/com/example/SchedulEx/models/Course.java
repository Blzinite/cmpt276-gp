package com.example.SchedulEx.models;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table(name="Courses")
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseID;
    private String courseDepartment;
    private String courseNumber;
    private int enrollment = 0;
    private Calendar examDateAndTime = new GregorianCalendar();
    private int duration = 0;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User instructor;

    public Course()
    {

    }

    public Course(String department, String number)
    {
        if(department.isEmpty() || number.isEmpty())
        {
            System.out.println("Department or number is empty");
        }

        this.courseDepartment = department;
        this.courseNumber = number;
    }

    public void UpdateCourseInformation(int enrollment, Calendar examDate, int duration)
    {


        if(enrollment > 0 && enrollment <= 999)
        {
            this.enrollment = enrollment;
        }
        else
        {
            this.enrollment = 0;
        }

        this.examDateAndTime = examDate;

        if(duration > 0 && duration <= 999)
        {
            this.duration = duration;
        }
        else
        {
            this.duration = 0;
        }
    }

    public void SetInstructor(User instructor)
    {
        this.instructor = instructor;
    }

    @Override
    public String toString()
    {
        return courseDepartment.trim() + " " + courseNumber.trim();
    }

    public int GetEnrollment()
    {
        return enrollment;
    }

    public String GetExamDate()
    {
        String year = String.format("%04d", examDateAndTime.get(Calendar.YEAR));
        String month = String.format("%02d", examDateAndTime.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", examDateAndTime.get(Calendar.DAY_OF_MONTH));
        return year + "-" + month + "-" + day;
    }

    public String GetStartTime()
    {
        int hour = examDateAndTime.get(Calendar.HOUR_OF_DAY);
        int minute = examDateAndTime.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minute);
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

    public int GetCourseID()
    {
        return courseID;
    }
}
