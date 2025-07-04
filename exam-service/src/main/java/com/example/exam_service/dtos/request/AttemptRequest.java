package com.example.exam_service.dtos.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttemptRequest {
    private Long examId;
    private Long studentId;
    private List<AnswerRequest> answers;
    
}
