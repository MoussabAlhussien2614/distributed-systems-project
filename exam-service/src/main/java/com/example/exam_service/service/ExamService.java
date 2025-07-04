package com.example.exam_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.exam_service.dtos.request.ExamRequest;
import com.example.exam_service.dtos.request.OptionRequest;
import com.example.exam_service.dtos.request.QuestionRequest;
import com.example.exam_service.dtos.response.ExamResponse;
import com.example.exam_service.dtos.response.OptionResponse;
import com.example.exam_service.dtos.response.QuestionResponse;
import com.example.exam_service.models.Exam;
import com.example.exam_service.models.Option;
import com.example.exam_service.models.Question;
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
            .map((Exam e) ->ExamResponse.builder()
                .id(e.getId())
                .courseId(e.getCourseId())
                .questions(e.getQuestions().stream()
                    .map((Question quest) -> QuestionResponse.builder()
                        .id(quest.getId())
                        .examId(quest.getExam().getId())
                        .content(quest.getContent())
                        .points(quest.getPoints())
                        .options(quest.getOptions().stream()
                            .map((Option opt) -> OptionResponse.builder()
                                .id(opt.getId())
                                .questionId(opt.getQuestion().getId())
                                .isCorrect(opt.getIsCorrect())  
                                .content(opt.getContent())
                                .build())
                            .collect(Collectors.toList()))
                        .build())
                    .collect(Collectors.toList()))
                .build())
            .collect(Collectors.toList());
        return exams;
    }

    public ExamResponse create(ExamRequest request){
        Exam exam = examRepository.save(Exam.builder()
            .courseId(request.getCourseId())
            .build());

        List<Question> questions = request.getQuestions().stream()
            .map((QuestionRequest q) -> {
                Question quest = questionRepository.save(Question.builder()
                    .exam(exam)
                    .points(q.getPoints())
                    .content(q.getContent())
                    .build());
                List<Option> options = q.getOptions().stream()
                    .map((OptionRequest opt) -> Option.builder()
                        .question(quest)
                        .isCorrect(opt.getIsCorrect())
                        .content(opt.getContent())
                        .build())
                    .collect(Collectors.toList());
                optionRepository.saveAll(options);
                quest.setOptions(options);
                return quest;    
            })
            .collect(Collectors.toList());
        questionRepository.saveAll(questions);
        exam.setQuestions(questions);

        return ExamResponse.builder()
            .id(exam.getId())
            .courseId(exam.getCourseId())
            .questions(exam.getQuestions().stream()
                .map((Question quest) -> QuestionResponse.builder()
                    .id(quest.getId())
                    .examId(quest.getExam().getId())
                    .content(quest.getContent())
                    .points(quest.getPoints())
                    .options(quest.getOptions().stream()
                        .map((Option opt) -> OptionResponse.builder()
                            .id(opt.getId())
                            .questionId(opt.getQuestion().getId())
                            .isCorrect(opt.getIsCorrect())  
                            .content(opt.getContent())
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .collect(Collectors.toList()))
            .build();
    }
}
