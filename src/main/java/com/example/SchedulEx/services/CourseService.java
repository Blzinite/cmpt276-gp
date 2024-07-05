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

import java.util.List;
import java.util.Map;

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
}
