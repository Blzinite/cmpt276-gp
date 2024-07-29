package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.AccessLevel;
import com.example.SchedulEx.models.Course;
import com.example.SchedulEx.models.RequestStatus;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
        Course course = courseService.getCourse(courseName).orElse(null);
        if (course == null) {
            return "Course not found";
        }
        JSONArray dates = course.getDates();
        JSONObject tmp = new JSONObject();
        tmp.put("userExamName", course.toString());
        tmp.put("userExamDur", course.getDuration());
        tmp.put("userExams", dates);
        tmp.put("otherExams", courseService.getAllExcept(courseName));
        return tmp.toJSONString();
    }

    // Add New Course as Instructor
    // Function: Allows users logged in as instructor to create a new course by providing course name, number, and enrollment
    @PostMapping("newCourse")
    public String newCourse(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        //verify user is logged in
        User user = (User) session.getAttribute("user");
        if(user==null){
            return "redirect:/action-panel";
        }

        // Verify user access level
        try
        {
            courseService.confirmUserAccessLevel(AccessLevel.INSTRUCTOR, session);
        }
        catch (IllegalStateException e)
        {
            System.err.println(e.getMessage());
            return courseService.getActionPanel(model, session);
        }

        // Create new course
        courseService.createNewCourse(params, session);
        model.addAttribute("isOpen", true);

        return courseService.getActionPanel(model, session);
    }

    // Delete course as instructor
    // Function: Removes a selected course from the database and refreshes action panel
    @PostMapping("deleteCourse{id}")
    public String deleteCourse(@PathVariable("id") int id, Model model, HttpSession session)
    {
        // Verify user access level
        try
        {
            courseService.confirmUserAccessLevel(AccessLevel.INSTRUCTOR, session);
        }
        catch (IllegalStateException e)
        {
            System.err.println(e.getMessage());
            return courseService.getActionPanel(model, session);
        }

        // Delete the course
        courseService.deleteCourse(id, session);
        model.addAttribute("isOpen", true);

        return courseService.getActionPanel(model, session);
    }

    @GetMapping("admin/{course}")
    public String getCourseInfo(@PathVariable("course") String course, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return courseService.getActionPanel(model, session);
        }
        if(user.getAccessLevel() != AccessLevel.ADMIN){
            return courseService.getActionPanel(model, session);
        }
        Course courseObj = courseService.getCourse(course).orElse(null);
        if (courseObj == null) {
            return courseService.getActionPanel(model, session);
        }
        User instructor = courseService.getInstructor(courseObj).orElse(null);
        if (instructor == null) {
            return courseService.getActionPanel(model, session);
        }
        Map<Integer, List<String>> overlaps = courseService.getOverlaps(courseObj);
        if(!overlaps.isEmpty()){
            if(!overlaps.get(1).isEmpty()){
                model.addAttribute("overlaps1", overlaps.get(1));
            }
            if(!overlaps.get(2).isEmpty()){
                model.addAttribute("overlaps2", overlaps.get(2));
            }
            if(!overlaps.get(3).isEmpty()){
                model.addAttribute("overlaps3", overlaps.get(3));
            }
        }
        model.addAttribute("instructor", instructor);
        model.addAttribute("course", courseObj);
        return "viewCourse";
    }

    @PostMapping("updateStatus/{course}")
    public String updateCourseStatus(@PathVariable("course") String course, @RequestParam Map<String, String> params, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return courseService.getActionPanel(model, session);
        }
        if(user.getAccessLevel() != AccessLevel.ADMIN){
            return courseService.getActionPanel(model, session);
        }
        Course courseObj = courseService.getCourse(course).orElse(null);
        if (courseObj == null) {
            return courseService.getActionPanel(model, session);
        }
        int newStatus = Integer.parseInt(params.get("status"));
        if(newStatus == RequestStatus.ACCEPTED_CUSTOM_TIME){
            courseObj = courseService.updateCustomTime(courseObj, params.get("dateOverride"), params.get("timeOverride"));
        }
        if(courseObj == null){
            return courseService.getActionPanel(model, session);
        }
        courseService.updateRequestStatus(courseObj, newStatus);
        return courseService.getActionPanel(model, session);
    }

    @PostMapping("submitExamRequest")
    public String updateCourseInformation(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        courseService.updateCourseInformation(params, model, session);
        return courseService.getActionPanel(model, session);
    }
}
