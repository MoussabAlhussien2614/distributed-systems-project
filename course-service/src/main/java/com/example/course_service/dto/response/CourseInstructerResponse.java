package com.example.course_service.dto.response;

import java.math.BigDecimal;

import com.example.course_service.model.CourseInstructer;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CourseInstructerResponse{ 
    private Long id;
    private String name;
    private Long instructerId;
    private BigDecimal tutionFee;
    private Boolean isApproved;
    private CourseInstructer courseInstructer;

    
}
