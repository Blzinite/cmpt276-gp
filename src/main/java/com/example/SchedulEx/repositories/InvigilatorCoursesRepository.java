package com.example.SchedulEx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SchedulEx.models.*;

import java.util.Optional;

public interface InvigilatorCoursesRepository extends JpaRepository<InvigilatorCourses, Integer> {
    public Optional<InvigilatorCourses> getInvigilatorCoursesByInvigilator(User invigilator);
}