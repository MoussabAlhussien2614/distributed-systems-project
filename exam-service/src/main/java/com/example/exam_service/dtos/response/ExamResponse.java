package com.example.exam_service.dtos.response;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Long id;
    private Long courseId;
    private Integer passingLimit;
    private List<QuestionResponse> questions;
}
