package com.example.accountservice.dto;

import com.example.accountservice.constant.AccountType;
import com.example.accountservice.constant.Currency;
import com.example.accountservice.util.GenerateAccountNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AccountData {

    private String userId;

    private GenerateAccountNumber accountNumber;

    private String accountHolder;

    private AccountType accountType;

    private Currency currency;

}
