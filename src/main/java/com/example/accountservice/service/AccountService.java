package com.example.accountservice.service;

import com.example.accountservice.constant.Status;
import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.AccountDto;

import java.math.BigDecimal;

public interface AccountService {

    void executeMoneyTransfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount);
    AccountDto getAccountById(Long id);
    AccountDto openAccount(AccountData accountData);
    AccountDto closeAccount(Long accountId);
    AccountDto updateBalance(Long accountId, BigDecimal amount);
    AccountDto changeStatus(Long accountId, Status status);

}
