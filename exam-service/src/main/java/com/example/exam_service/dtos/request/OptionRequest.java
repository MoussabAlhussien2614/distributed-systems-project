package com.example.exam_service.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {
    private Long questionId;
    private String content;
    private Boolean isCorrect;
}
