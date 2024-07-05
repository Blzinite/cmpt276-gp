package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.Course;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("newCourse")
    public String NewCourse(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        courseService.CreateNewCourse(params, model, session);
        model.addAttribute("isOpen", true);

        User user = (User) session.getAttribute("user");
        System.out.println(user.GetCourses());



        return courseService.GetActionPanel(model, session);
    }
}
