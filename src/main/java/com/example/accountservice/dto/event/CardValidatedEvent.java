package com.example.accountservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardValidatedEvent {

    private String transactionId;

    private Long sourceAccountId;

    private Long targetAccountId;

    private BigDecimal amount;

    private String currency;
}
