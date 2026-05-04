package com.example.accountservice.service;

import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.CreateAccountDto;

public interface AccountService {

    CreateAccountDto openAccount(AccountData accountData);

}
