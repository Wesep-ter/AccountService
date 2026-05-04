package com.example.accountservice.util;

import com.example.accountservice.dto.AccountData;

public interface GenerateAccountNumber {
    String generate(AccountData accountData);
}
