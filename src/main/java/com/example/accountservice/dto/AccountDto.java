package com.example.accountservice.dto;

import com.example.accountservice.constant.AccountType;
import com.example.accountservice.constant.Currency;
import com.example.accountservice.constant.Status;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String userId;
    private AccountType accountType;
    private String accountNumber;
    private Currency currency;
    private BigDecimal balance;
    private Status status;
    private LocalDateTime statusChangeTime;
}
