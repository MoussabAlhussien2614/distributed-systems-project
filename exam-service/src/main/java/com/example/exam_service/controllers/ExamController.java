package com.example.exam_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exam_service.dtos.request.AttemptRequest;
import com.example.exam_service.dtos.request.ExamRequest;
import com.example.exam_service.dtos.response.AttemptResponse;
import com.example.exam_service.dtos.response.ExamResponse;
import com.example.exam_service.service.ExamService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private ExamService examService;

    public ExamController(ExamService examService){
        this.examService = examService;
    }

    @GetMapping()
    public List<ExamResponse> list() {
        var response = examService.list();
        return response;
    }
    
    @PostMapping()
    public ExamResponse create(@RequestBody ExamRequest request) {
        var response  = examService.create(request);
        return response;
    }

    @PostMapping("/{id}/attempts")
    public AttemptResponse createAttempt(@PathVariable Long id, @RequestBody AttemptRequest request) {
        var response  = examService.createAttempt(id, request);
        return response;
    }

    @GetMapping("/{id}/attempts")
    public List<AttemptResponse> listAttempts(@PathVariable Long id) {
        var response  = examService.listAttempts(id);
        return response;
    }

    

    
}
