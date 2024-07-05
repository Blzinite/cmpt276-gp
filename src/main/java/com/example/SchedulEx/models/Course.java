package com.example.SchedulEx.models;

import jakarta.persistence.*;

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

    public void SetInstructor(User instructor)
    {
        this.instructor = instructor;
    }

    @Override
    public String toString()
    {
        return courseDepartment.trim() + " " + courseNumber.trim();
    }
}
