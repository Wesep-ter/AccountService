package com.example.accountservice.service;

import com.example.accountservice.constant.Status;
import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.AccountDto;

import java.math.BigDecimal;

public interface AccountService {

    AccountDto openAccount(AccountData accountData);
    void closeAccount();
    void updateBalance(Long accountId, BigDecimal amount);
    void changeStatus(Long accountId, Status status);

}
