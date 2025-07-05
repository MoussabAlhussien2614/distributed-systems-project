package com.example.subscription_service.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BalanceResponse {
    private Long id;
    private Long userId;
    private Long balance;
}
