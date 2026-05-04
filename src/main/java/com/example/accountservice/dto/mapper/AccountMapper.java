package com.example.accountservice.dto.mapper;

import com.example.accountservice.dto.CreateAccountDto;
import com.example.accountservice.entity.Account;

public class AccountMapper {

    public static CreateAccountDto toDto(Account account){
        return CreateAccountDto.builder()
                .userId(account.getUserId())
                .accountType(account.getAccountType())
                .accountNumber(account.getAccountNumber())
                .currency(account.getCurrency())
                .initialDeposit(account.getBalance()).build();
    }
}
