package com.example.accountservice.dto;

import com.example.accountservice.constant.AccountType;
import com.example.accountservice.constant.Currency;
import lombok.*;
import java.math.BigDecimal;

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
}
