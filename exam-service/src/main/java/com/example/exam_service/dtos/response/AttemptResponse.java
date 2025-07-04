package com.example.exam_service.dtos.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttemptResponse {
    private Long id;
    private Long examId;
    private Long studentId;
    private List<AnswerResponse> answers;
    
}
