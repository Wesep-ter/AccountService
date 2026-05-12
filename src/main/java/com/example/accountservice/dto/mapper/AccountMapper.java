package com.example.accountservice.dto.mapper;

import com.example.accountservice.dto.AccountDto;
import com.example.accountservice.entity.Account;

public class AccountMapper {

    public static AccountDto toDto(Account account){
        return AccountDto.builder()
                .userId(account.getUserId())
                .accountType(account.getAccountType())
                .accountNumber(account.getAccountNumber())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .status(account.getStatus())
                .statusChangeTime(account.getStatusChangeTime())
                .build();
    }
}
