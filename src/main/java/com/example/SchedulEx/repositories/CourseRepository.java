package com.example.SchedulEx.repositories;

import com.example.SchedulEx.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer>
{

}
