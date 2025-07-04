package com.example.exam_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponse {
    private Long id;
    private Long questionId;
    private String content;
    private Boolean isCorrect;
}
