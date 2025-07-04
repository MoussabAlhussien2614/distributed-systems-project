package com.example.exam_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.exam_service.models.Exam;


@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>{

    
    
}
