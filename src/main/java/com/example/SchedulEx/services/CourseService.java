package com.example.SchedulEx.services;

import com.example.SchedulEx.helpers.UnixHelper;
import com.example.SchedulEx.models.*;
import com.example.SchedulEx.repositories.CourseRepository;
import com.example.SchedulEx.repositories.InvigilatorDataRepository;
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
    private final InvigilatorDataRepository invigRepo;

    @Autowired
    public CourseService(UserRepository userRepository, CourseRepository courseRepository, InvigilatorDataRepository invigRepo)
    {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.invigRepo = invigRepo;
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
    public void createNewCourse(Map<String, String> params, HttpSession session)
    {
        // Get reference to instructor
        User instructor = (User) session.getAttribute("user");

        // Create new course
        String department = params.get("department");
        int number = Integer.parseInt(params.get("number"));
        int enrollment = Integer.parseInt(params.get("enrollment"));

        Course newCourse = new Course(department + "-" + number, enrollment, instructor);

        // Add it to the user
        instructor.addCourse(newCourse);

        courseRepository.save(newCourse);
    }

    @Transactional
    public List<Course> getUserCourses(Model model, HttpSession session)
    {
        User user = (User) session.getAttribute("user");

        if(user == null)
        {
            return null;
        }
        else if(user.getAccessLevel() == AccessLevel.INSTRUCTOR)
        {
            return user.getCourses();
        }
        else
        {
            return null;
        }
    }

    @Transactional
    public List<Course> getAccepted(){
        return courseRepository.findByRequestStatusBetween(RequestStatus.ACCEPTED_TIME_ONE, RequestStatus.ACCEPTED_CUSTOM_TIME);
    }


    //precondition: 'course' must be not null
    //returns all accepted courses in db with times overlapping any course.dateX
    //return map form will be
    // {1; list of course names overlapping with course.dateOne,
    // 2; list of course names overlapping with course.dateTwo,
    // 3; list of course names overlapping with course.dateThree}
    @Transactional
    public Map<Integer, List<String>> getOverlaps(Course course){
        if(course == null){
            return null;
        }
        List<Course> otherCourses = getAccepted();
        otherCourses.remove(course);
        Map<Integer, List<String>> overlaps = new HashMap<>();
        List<String> overlapOne = new ArrayList<>();
        List<String> overlapTwo = new ArrayList<>();
        List<String> overlapThree = new ArrayList<>();
        for(Course otherCourse : otherCourses){
            if(course.overlaps(otherCourse, 1)){
                overlapOne.add(otherCourse.getCourseName());
            }
            if(course.overlaps(otherCourse, 2)){
                overlapTwo.add(otherCourse.getCourseName());
            }
            if(course.overlaps(otherCourse, 3)){
                overlapThree.add(otherCourse.getCourseName());
            }
        }
        overlaps.put(1,overlapOne);
        overlaps.put(2,overlapTwo);
        overlaps.put(3,overlapThree);
        return overlaps;
    }


    @Transactional
    public Optional<Course> getCourse(String courseName){
        return courseRepository.findByCourseName(courseName);
    }

    @Transactional
    public Optional<User> getInstructor(Course course){
        return userRepository.findById(course.getInstructorID());
    }

    public String getActionPanel(Model model, HttpSession session)
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
                model.addAttribute("approved", courseRepository.findByRequestStatusBetween(
                        RequestStatus.ACCEPTED_TIME_ONE,
                        RequestStatus.ACCEPTED_CUSTOM_TIME
                ));
                return "admin";
            }
            case INVIGILATOR -> {
                model.addAttribute("currentUser", curr);
                List<Course> courses = new ArrayList<>();
                InvigilatorData id = invigRepo.getInvigilatorDataByInvigilator(curr).orElse(null);
                if(id != null){
                    for (int i : id.getCourseIds()) {
                        courseRepository.findById(i).ifPresent(courses::add);
                    }
                }
                model.addAttribute("courses", courses);
//                for (Course course : curr.getCourses()) {
//                    System.out.println(course + "Added to attribute");
//                }
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
    public boolean confirmUserAccessLevel(AccessLevel accessLevel, HttpSession session) throws IllegalArgumentException
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
    public void deleteCourse(int id, HttpSession session)
    {
        User user = (User) session.getAttribute("user");

        Course course = courseRepository.findById(id).orElse(null);
        if (course != null)
        {
            user.removeCourse(course);
            course.removeInstructor();
            courseRepository.deleteById(course.getCourseID());
            userRepository.save(user);
        }
    }

    public void updateCourseInformation(Map<String, String> params, Model model, HttpSession session)
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
                user.removeCourse(course);
                course.updateCourse(params);
                courseRepository.save(course);
                user.addCourse(course);
                userRepository.save(user);
            }
            else
            {
                System.out.println("Course not found");
            }
        }
    }

    public String appendInvigilators(Model model, HttpSession session) {
        model.addAttribute("invigilators", userRepository.findByAccessLevel("invigilator"));
        return "approvedCourse";
    }

    public Course updateCustomTime(Course courseObj, String dateOverride, String timeOverride) {
        User instructor = userRepository.findById(courseObj.getInstructorID()).orElse(null);
        if(instructor == null){
            return null;
        }
        instructor.removeCourse(courseObj);
        courseObj.setCustomTime(UnixHelper.parseDate(dateOverride, timeOverride));
        courseRepository.save(courseObj);
        instructor.addCourse(courseObj);
        userRepository.save(instructor);
        return courseObj;
    }

    public void updateRequestStatus(Course courseObj, int newStatus) {
        User instructor = userRepository.findById(courseObj.getInstructorID()).orElse(null);
        if(instructor == null){
            return;
        }
        instructor.removeCourse(courseObj);
        courseObj.setRequestStatus(newStatus);
        courseRepository.save(courseObj);
        instructor.addCourse(courseObj);
        userRepository.save(instructor);
    }

    public void addCourseToInvigilator(Map<String, String> params, Model model, HttpSession session) {
        courseRepository.findByCourseName(params.get("course")).ifPresent(course -> {
            for (String key : params.keySet()) {
                if (!key.equals("course")) {
                    userRepository.findByEmail(params.get(key)).ifPresent(user -> {
                        InvigilatorData id = invigRepo.getInvigilatorDataByInvigilator(user).orElse(null);
                        if (id != null) {
                            id.addCourse(course.getCourseID());
                            System.out.println("ADDED");
                        } else {
                            System.out.println("CANT FIND");
                        }
                    });
                }
            }
        });
    }
}
