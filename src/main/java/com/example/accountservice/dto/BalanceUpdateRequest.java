package com.example.accountservice.dto;
import com.example.accountservice.constant.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceUpdateRequest {
    private BigDecimal amount;
    private Currency currency;
    private String transactionId;
}
