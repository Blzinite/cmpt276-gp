package com.example.SchedulEx.services;

import com.example.SchedulEx.models.*;
import com.example.SchedulEx.repositories.CourseRepository;
import com.example.SchedulEx.repositories.ExamRepository;
import com.example.SchedulEx.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class CourseService
{
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(UserRepository userRepository, CourseRepository courseRepository)
    {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public void CreateNewCourse(Map<String, String> params, Model model, HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if (user == null)
        {
            System.exit(5);
        }

        Course newCourse = new Course(params.get("department"), params.get("number"));
        user.AddCourse(newCourse);
        courseRepository.save(newCourse);
    }

    @Transactional
    public List<Course> GetUserCourses(Model model, HttpSession session)
    {
        User user = (User) session.getAttribute("user");

        if(user == null)
        {
            return null;
        }
        else if(user.getAccessLevel() == AccessLevel.PROFESSOR)
        {
            System.out.println("Called get user courses for a professor");

            return user.GetCourses();
        }
        else
        {
            return null;
        }
    }

    public String GetActionPanel(Model model, HttpSession session)
    {
        User curr = (User) session.getAttribute("user");
        if(curr == null){
            return "login";
        }
        switch(curr.getAccessLevel()){
            case AccessLevel.ADMIN -> {
                model.addAttribute("currentUser", curr);
                model.addAttribute("users", userRepository.findAll());
                return "admin";
            }
            case AccessLevel.INVIGILATOR -> {
                model.addAttribute("currentUser", curr);
                return "invigilator";
            }
            case AccessLevel.PROFESSOR -> {
                model.addAttribute("currentUser", curr);
                return "professor";
            }
            default -> {
                return "login";
            }
        }
    }

    public void UpdateCourseInformation(Map<String, String> params, Model model, HttpSession session)
    {
        // Get info from form submission
        int instructorID = -1;
        int courseID = -1;
        int enrollment = -1;
        int duration = -1;

        try
        {
            instructorID = Integer.parseInt(params.get("courseInstructorID"));
            courseID = Integer.parseInt(params.get("courseID"));
            enrollment = Integer.parseInt(params.get("enrollment"));
            duration = Integer.parseInt(params.get("duration"));
        }
        catch (Exception e)
        {
            System.out.println("There was an error casting a param to its intended type");
        }

        // Construct new calendar
        Calendar date = new GregorianCalendar();
        int year = Integer.parseInt(params.get("examDate").substring(0, 4));
        int month = Integer.parseInt(params.get("examDate").substring(5, 7)) - 1;
        int day = Integer.parseInt(params.get("examDate").substring(8, 10));
        date.set(year, month, day);

        // Verify that the instructor who teaches the course is the one trying to update it
        User user = (User) session.getAttribute("user");
        if(user == null)
        {
            System.out.println("Error: There is no user");
            return;
        }

        if(user.getUid() == instructorID)
        {
            System.out.println("Course instructor ID matches submission user ID");
            System.out.println("Course ID: " + courseID);

            Optional<Course> optionalCourse = courseRepository.findById(courseID);
            if(optionalCourse.isPresent())
            {
                Course course = optionalCourse.get();

                course.UpdateCourseInformation(enrollment, date, duration);
                courseRepository.save(course);
                user.SetCourses(courseRepository.findAll());
            }
            else
            {
                System.out.println("Course not found");
            }
        }
    }
}
