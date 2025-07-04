package com.example.exam_service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.exam_service.dtos.request.ExamRequest;
import com.example.exam_service.dtos.response.ExamResponse;
import com.example.exam_service.models.Exam;
import com.example.exam_service.repositories.AttemptRepository;
import com.example.exam_service.repositories.ExamRepository;
import com.example.exam_service.repositories.OptionRepository;
import com.example.exam_service.repositories.QuestionRepository;

@Service
public class ExamService {
    private AttemptRepository attemptRepository;
	private ExamRepository examRepository;
	private QuestionRepository questionRepository;
	private OptionRepository optionRepository;

	public ExamService(
        AttemptRepository attemptRepository,
        ExamRepository examRepository,
        QuestionRepository questionRepository, 
        OptionRepository optionRepository){
            this.attemptRepository = attemptRepository;
            this.examRepository = examRepository;
            this.questionRepository = questionRepository;
            this.optionRepository = optionRepository;
    }

    public List<ExamResponse> list(){
        List<ExamResponse> exams = examRepository.findAll().stream()
            .map((Exam e) -> ExamResponse.builder()
                
                .build())
            .collect(Collectors.toList());
        return new LinkedList<>();
    }

    // public ExamResponse create(ExamRequest request){
        
    // }
}
