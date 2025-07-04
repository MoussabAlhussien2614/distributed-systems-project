package com.example.exam_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.exam_service.models.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>{

    
    
}
