package com.example.accountservice.service;

import com.example.accountservice.constant.Status;
import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.AccountDto;
import com.example.accountservice.dto.mapper.AccountMapper;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.AuditLog;
import com.example.accountservice.repositories.AccountRepository;
import com.example.accountservice.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public AccountDto openAccount(AccountData accountData){
        Account account = createAccount(accountData);
        log.info("Счёт открыт пользователем с номером id: " + accountData.getUserId());
        return AccountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public void closeAccount(){

    }

    @Override
    @Transactional
    public void updateBalance(Long accountId, BigDecimal amount){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Счёт не найден"));
        BigDecimal newBalance = account.getBalance().add(amount);

        if (Status.BLOCKED.equals(account.getStatus())){
            throw  new RuntimeException("счёт заблокирован");
        }

        if (newBalance.signum() < 0){
            throw new RuntimeException("недостаточно средств");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
        saveAuditLog(accountId, amount);
    }

    @Override
    @Transactional
    public void changeStatus(Long accountId, Status status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Счёт не найден"));
        account.setStatus(status);
        accountRepository.save(account);
    }


    private Account createAccount(AccountData accountData){
        return Account.builder()
                .userId(accountData.getUserId())
                .currency(accountData.getCurrency())
                .accountNumber(accountData.getAccountNumber().generate(accountData))
                .accountHolder(accountData.getAccountHolder())
                .accountType(accountData.getAccountType())
                .balance(BigDecimal.ZERO)
                .isActive(true)
                .status(Status.ACTIVE)
                .build();
    }

    private void saveAuditLog(Long accountId, BigDecimal amount){
        AuditLog log = new AuditLog();
        log.setAccountId(accountId);
        log.setAmount(amount);
        log.setCreatedAt(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
