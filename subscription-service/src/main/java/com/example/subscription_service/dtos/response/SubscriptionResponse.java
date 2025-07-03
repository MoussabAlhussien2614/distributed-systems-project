package com.example.subscription_service.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SubscriptionResponse {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String status;
    
}
