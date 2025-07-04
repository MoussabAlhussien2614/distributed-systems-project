package com.example.exam_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.exam_service.models.Attempt;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long>{

    
    
}
