package com.example.SchedulEx.repositories;

import com.example.SchedulEx.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer>
{
    Optional<Course> findByCourseName(String courseName);
    List<Course> findByCourseNameNot(String courseName);
}
