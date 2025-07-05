package com.example.subscription_service.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CourseFetched {
    private Long id;
    private String name;
    private Long instructerId;
    private BigDecimal tutionFee;
    private Boolean isApproved;

    
}
