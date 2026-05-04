package com.example.accountservice.service;

import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.CreateAccountDto;
import com.example.accountservice.dto.mapper.AccountMapper;
import com.example.accountservice.entity.Account;
import com.example.accountservice.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public CreateAccountDto openAccount(AccountData accountData) {
        Account account = createAccount(accountData);
        log.info("Счёт открыт пользователем с номером id: " + accountData.getUserId());
        return AccountMapper.toDto(accountRepository.save(account));
    }

    private Account createAccount(AccountData accountData){
        return Account.builder()
                .userId(accountData.getUserId())
                .currency(accountData.getCurrency())
                .accountNumber(accountData.getAccountNumber().generate(accountData))
                .accountHolder(accountData.getAccountHolder())
                .accountType(accountData.getAccountType())
                .build();
    }
}
