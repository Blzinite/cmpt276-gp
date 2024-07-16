package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.AccessLevel;
import com.example.SchedulEx.models.Course;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.repositories.CourseRepository;
import com.example.SchedulEx.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping(value="course-info/{courseName}", method=RequestMethod.GET)
    @ResponseBody
    public String courseInfo(@PathVariable("courseName") String courseName) {
        Course course = courseService.GetCourse(courseName).orElse(null);
        if (course == null) {
            return "Course not found";
        }
        return course.getJSON().toJSONString();
    }

    // Add New Course as Instructor
    // Function: Allows users logged in as instructor to create a new course by providing course name, number, and enrollment
    @PostMapping("newCourse")
    public String NewCourse(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        // TODO: check if user logged in...
        // Bug. return get action panel should return to login if they are not logged in, but I got an error. Not sure how to reproduce.

        // Verify user access level
        try
        {
            courseService.ConfirmUserAccessLevel(AccessLevel.INSTRUCTOR, session);
        }
        catch (IllegalStateException e)
        {
            System.err.println(e.getMessage());
            return courseService.GetActionPanel(model, session);
        }

        // Create new course
        courseService.CreateNewCourse(params, session);
        model.addAttribute("isOpen", true);

        return courseService.GetActionPanel(model, session);
    }

    // Delete course as instructor
    // Function: Removes a selected course from the database and refreshes action panel
    @PostMapping("deleteCourse{id}")
    public String DeleteCourse(@PathVariable("id") int id, Model model, HttpSession session)
    {
        // Verify user access level
        try
        {
            courseService.ConfirmUserAccessLevel(AccessLevel.INSTRUCTOR, session);
        }
        catch (IllegalStateException e)
        {
            System.err.println(e.getMessage());
            return courseService.GetActionPanel(model, session);
        }

        // Delete the course
        courseService.DeleteCourse(id, session);
        model.addAttribute("isOpen", true);

        return courseService.GetActionPanel(model, session);
    }

    @PostMapping("submitExamRequest")
    public String UpdateCourseInformation(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        courseService.UpdateCourseInformation(params, model, session);
        model.addAttribute("isOpen", true);

        return courseService.GetActionPanel(model, session);
    }
}
