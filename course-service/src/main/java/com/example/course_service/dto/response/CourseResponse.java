package com.example.course_service.dto.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CourseResponse {
    private Long id;
    private String name;
    private Long instructerId;
    private BigDecimal tutionFee;
    private Boolean isApproved;

    
}
