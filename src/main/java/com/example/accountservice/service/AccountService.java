package com.example.accountservice.service;

import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.CreateAccountDto;

import java.math.BigDecimal;

public interface AccountService {

    CreateAccountDto openAccount(AccountData accountData);
    void closeAccount();
    void updateBalance(Long accountId, BigDecimal amount);


}
