package com.example.SchedulEx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SchedulEx.models.*;

import java.util.Optional;

public interface InvigilatorDataRepository extends JpaRepository<InvigilatorData, Integer> {
    public Optional<InvigilatorData> getInvigilatorDataByInvigilator(User invigilator);
}