package com.example.SchedulEx.services;

import com.example.SchedulEx.helpers.UnixHelper;
import com.example.SchedulEx.models.*;
import com.example.SchedulEx.repositories.CourseRepository;
import com.example.SchedulEx.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
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
    public JSONArray getAllExcept(String name){
        List<Course> all = courseRepository.findByCourseNameNot(name);
        JSONArray tmp = new JSONArray();
        for(Course course : all){
            tmp.addAll(course.getDates());
        }
        return tmp;
    }

    @Transactional
    public void CreateNewCourse(Map<String, String> params, HttpSession session)
    {
        // Get reference to instructor
        User instructor = (User) session.getAttribute("user");

        // Create new course
        String department = params.get("department");
        int number = Integer.parseInt(params.get("number"));
        int enrollment = Integer.parseInt(params.get("enrollment"));

        Course newCourse = new Course(department + "-" + number, enrollment, instructor);

        // Add it to the user
        instructor.AddCourse(newCourse);

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
        else if(user.getAccessLevel() == AccessLevel.INSTRUCTOR)
        {
            return user.GetCourses();
        }
        else
        {
            return null;
        }
    }

    @Transactional
    public List<Course> getAccepted(){
        return courseRepository.findByRequestStatusBetween(RequestStatus.ACCEPTED_TIME_ONE, RequestStatus.ACCEPTED_TIME_THREE);
    }

    @Transactional
    public Optional<Course> GetCourse(String courseName){
        return courseRepository.findByCourseName(courseName);
    }

    @Transactional
    public Optional<User> getInstructor(Course course){
        return userRepository.findById(course.GetInstructorID());
    }

    public String GetActionPanel(Model model, HttpSession session)
    {
        User curr = (User) session.getAttribute("user");
        if(curr == null){
            return "login";
        }
        switch(curr.getAccessLevel()){
            case ADMIN -> {
                model.addAttribute("currentUser", curr);
                model.addAttribute("users", userRepository.findAll());
                model.addAttribute("pending", courseRepository.findByRequestStatus(RequestStatus.PENDING));
                return "admin";
            }
            case INVIGILATOR -> {
                model.addAttribute("currentUser", curr);
                return "invigilator";
            }
            case INSTRUCTOR -> {
                model.addAttribute("currentUser", curr);
                model.addAttribute("courses", courseRepository.findByInstructor(curr));
                return "instructor";
            }
            default -> {
                return "login";
            }
        }
    }

    // Verifies user access level
    // Function: If the user is not of the proper access level given as input, throws exception.
    public boolean ConfirmUserAccessLevel(AccessLevel accessLevel, HttpSession session) throws IllegalArgumentException
    {
        User user = (User) session.getAttribute("user");
        if (user == null)
        {
            return false;
        }
        else if (user.getAccessLevel() == accessLevel)
        {
            return true;
        }
        throw new IllegalArgumentException();
    }

    // Delete course from repo
    // make sure to remove all references between the user and course to be deleted
    // then delete the course from course repo AND update user in user repo
    // Or else things break.
    @Transactional
    public void DeleteCourse(int id, HttpSession session)
    {
        User user = (User) session.getAttribute("user");

        Course course = courseRepository.findById(id).orElse(null);
        if (course != null)
        {
            user.RemoveCourse(course);
            course.RemoveInstructor();
            courseRepository.deleteById(course.GetCourseID());
            userRepository.save(user);
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

        // Verify that the instructor who teaches the course is the one trying to update it
        User user = (User) session.getAttribute("user");
        if(user == null)
        {
            System.out.println("Error: There is no user");
            return;
        }

        if(user.getUid() == instructorID)
        {
            Optional<Course> optionalCourse = courseRepository.findById(courseID);
            if(optionalCourse.isPresent())
            {
                Course course = optionalCourse.get();
                user.RemoveCourse(course);
                course.updateCourse(params);
                courseRepository.save(course);
                user.AddCourse(course);
                userRepository.save(user);
            }
            else
            {
                System.out.println("Course not found");
            }
        }
    }

    public Course updateCustomTime(Course courseObj, String dateOverride, String timeOverride) {
        User instructor = userRepository.findById(courseObj.GetInstructorID()).orElse(null);
        if(instructor == null){
            return null;
        }
        instructor.RemoveCourse(courseObj);
        courseObj.setCustomTime(UnixHelper.parseDate(dateOverride, timeOverride));
        courseRepository.save(courseObj);
        instructor.AddCourse(courseObj);
        userRepository.save(instructor);
        return courseObj;
    }

    public void updateRequestStatus(Course courseObj, int newStatus) {
        User instructor = userRepository.findById(courseObj.GetInstructorID()).orElse(null);
        if(instructor == null){
            return;
        }
        instructor.RemoveCourse(courseObj);
        courseObj.setRequestStatus(newStatus);
        courseRepository.save(courseObj);
        instructor.AddCourse(courseObj);
        userRepository.save(instructor);
    }
}
