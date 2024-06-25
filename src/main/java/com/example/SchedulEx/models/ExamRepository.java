package com.example.SchedulEx.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    Optional<Exam> findByRequestId(Long requestId);
}
