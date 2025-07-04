package com.example.exam_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exam_service.dtos.request.ExamRequest;
import com.example.exam_service.dtos.response.ExamResponse;
import com.example.exam_service.service.ExamService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;



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
    
}
