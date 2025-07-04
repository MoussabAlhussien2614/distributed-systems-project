package com.example.exam_service.dtos.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ExamRequest {
    private Long courseId;
    private Integer passingLimit;
    private List<QuestionRequest> questions;
}
