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
public class QuestionRequest {
    private Long examId;
    private String content;
    private Integer points;
    private List<OptionRequest> options;
}
