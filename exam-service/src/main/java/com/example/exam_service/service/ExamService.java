package com.example.exam_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.exam_service.dtos.request.AnswerRequest;
import com.example.exam_service.dtos.request.AttemptRequest;
import com.example.exam_service.dtos.request.ExamRequest;
import com.example.exam_service.dtos.request.OptionRequest;
import com.example.exam_service.dtos.request.QuestionRequest;
import com.example.exam_service.dtos.response.AnswerResponse;
import com.example.exam_service.dtos.response.AttemptResponse;
import com.example.exam_service.dtos.response.ExamResponse;
import com.example.exam_service.dtos.response.OptionResponse;
import com.example.exam_service.dtos.response.QuestionResponse;
import com.example.exam_service.models.Answer;
import com.example.exam_service.models.Attempt;
import com.example.exam_service.models.Exam;
import com.example.exam_service.models.Option;
import com.example.exam_service.models.Question;
import com.example.exam_service.repositories.AnswerRepository;
import com.example.exam_service.repositories.AttemptRepository;
import com.example.exam_service.repositories.ExamRepository;
import com.example.exam_service.repositories.OptionRepository;
import com.example.exam_service.repositories.QuestionRepository;

@Service
public class ExamService {

    private final AnswerRepository answerRepository;
    private AttemptRepository attemptRepository;
	private ExamRepository examRepository;
	private QuestionRepository questionRepository;
	private OptionRepository optionRepository;

	public ExamService(
        AttemptRepository attemptRepository,
        ExamRepository examRepository,
        QuestionRepository questionRepository, 
        OptionRepository optionRepository,
        AnswerRepository answerRepository){
            this.answerRepository = answerRepository; 
            this.attemptRepository = attemptRepository;
            this.examRepository = examRepository;
            this.questionRepository = questionRepository;
            this.optionRepository = optionRepository;
    }

    public List<ExamResponse> list(){
        List<ExamResponse> exams = examRepository.findAll().stream()
            .map((Exam e) ->ExamResponse.builder()
                .id(e.getId())
                .passingLimit(e.getPassingLimit())
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
            .passingLimit(request.getPassingLimit())
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
            .passingLimit(exam.getPassingLimit())
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

    public AttemptResponse createAttempt(Long examId, AttemptRequest request){
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new RuntimeException("Exam with id {examId} not found."));
        List<Long> optoin_ids = request.getAnswers().stream()
            .map((AnswerRequest ans) -> ans.getOptionId())
            .collect(Collectors.toList());
        List<Option> options = optionRepository.findAllById(optoin_ids);

        Integer finalScore = 0;
        for (Option op : options) {
            if (op.getIsCorrect()){
                finalScore += op.getQuestion().getPoints();
            }
        }
        Attempt attempt = attemptRepository.save( Attempt.builder()
            .exam(exam)
            .studentId(request.getStudentId())
            .finalScore(finalScore)
            .result(finalScore > exam.getPassingLimit() ? "Passed" : "Failed")
            .build());
        List<Answer> answers = request.getAnswers().stream()
            .map((AnswerRequest ans) -> Answer.builder()
                .attempt(attempt)
                .option(optionRepository.findById(ans.getOptionId()).orElse(null))
                .build())
            .collect(Collectors.toList());
        answerRepository.saveAll(answers);
        return AttemptResponse.builder()
            .id(attempt.getId())
            .studentId(attempt.getStudentId())
            .finalScore(finalScore)
            .examId(attempt.getExam().getId())
            .result(attempt.getResult())
            .answers(answers.stream()
                .map((Answer ans) -> AnswerResponse.builder()
                    .id(ans.getId())
                    .optionId(ans.getOption().getId())
                    .optionContent(ans.getOption().getContent())
                    .build())
                .collect(Collectors.toList())
            )
            .build();
    }
    public List<AttemptResponse> listAttempts(Long examId){
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new RuntimeException("Exam with id {examId} not found."));
 
        List<Attempt> attempts = attemptRepository.findByExam(exam);

        return attempts.stream()
            .map((Attempt attempt) -> AttemptResponse.builder()
                .id(attempt.getId())
                .studentId(attempt.getStudentId())
                .examId(attempt.getExam().getId())
                .answers(attempt.getAnswers().stream()
                    .map((Answer ans) -> AnswerResponse.builder()
                        .id(ans.getId())
                        .optionId(ans.getOption().getId())
                        .optionContent(ans.getOption().getContent())
                        .build())
                    .collect(Collectors.toList())
                )
                .build())
            .collect(Collectors.toList());
    }
}
