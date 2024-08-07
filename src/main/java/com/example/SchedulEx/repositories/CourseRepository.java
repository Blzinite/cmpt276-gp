package com.example.SchedulEx.repositories;

import com.example.SchedulEx.models.Course;
import com.example.SchedulEx.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer>
{
    Optional<Course> findByCourseID(int id);
    Optional<Course> findByCourseName(String courseName);
    List<Course> findByCourseNameNot(String courseName);
    List<Course> findByRequestStatus(int requestStatus);
    List<Course> findByRequestStatusBetween(int first, int second);
    List<Course> findByInstructor(User instructor);
    List<Course> findByDateOneBetweenOrDateTwoBetweenOrDateThreeBetween(Long dateOne, Long dateOne2, Long dateTwo, Long dateTwo2, Long dateThree, Long dateThree2);
}