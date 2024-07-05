package com.example.SchedulEx.repositories;

import com.example.SchedulEx.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    Optional<Exam> findByRequestId(Long requestId);
}
