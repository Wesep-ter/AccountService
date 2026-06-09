package com.example.accountservice.service;

import com.example.accountservice.constant.Status;
import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.AccountDto;
import com.example.accountservice.dto.mapper.AccountMapper;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.AuditLog;
import com.example.accountservice.exception.AccountNotFoundException;
import com.example.accountservice.exception.BlockedAccountException;
import com.example.accountservice.exception.CloseAccountException;
import com.example.accountservice.exception.NotEnoughMoneyException;
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
    @Transactional(readOnly = true)
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Счёт с ID " + id + " не найден"));

        return AccountMapper.toDto(account);
    }

    @Override
    @Transactional
    public AccountDto openAccount(AccountData accountData){
        Account account = createAccount(accountData);
        log.info("Счёт открыт пользователем с id: " + accountData.getUserId());
        return AccountMapper.toDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountDto closeAccount(Long accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Счёт не найден"));
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new CloseAccountException("Нельзя закрыть счет с положительным балансом");
        }
        account.setStatus(Status.CLOSED);
        account.setStatusChangeTime(LocalDateTime.now());
        return AccountMapper.toDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountDto updateBalance(Long accountId, BigDecimal amount) {

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Счёт не найден"));

        if (Status.BLOCKED.equals(account.getStatus())) {
            throw new BlockedAccountException("Счёт заблокирован");
        }

        BigDecimal newBalance = account.getBalance().add(amount);

        if (newBalance.signum() < 0) {
            throw new NotEnoughMoneyException("Недостаточно средств на счете");
        }
        account.setBalance(newBalance);
        Account savedAccount = accountRepository.save(account);
        saveAuditLog(accountId, amount);
        return AccountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto changeStatus(Long accountId, Status status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Счёт не найден"));
        account.setStatus(status);
        account.setStatusChangeTime(LocalDateTime.now());
        return AccountMapper.toDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void executeMoneyTransfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount){
        log.info("Начало перевода со счета {} на счет {} на сумму {}", sourceAccountId, targetAccountId, amount);
        this.updateBalance(sourceAccountId, amount.negate());
        this.updateBalance(targetAccountId, amount);
        log.info("Перевод успешно выполнен на уровне СУБД");
    }

    private Account createAccount(AccountData accountData){
        return Account.builder()
                .userId(accountData.getUserId())
                .currency(accountData.getCurrency())
                .accountNumber(accountData.getAccountNumber().generate(accountData))
                .accountHolder(accountData.getAccountHolder())
                .accountType(accountData.getAccountType())
                .balance(BigDecimal.ZERO)
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
