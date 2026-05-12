package com.example.accountservice.controller;

import com.example.accountservice.constant.Status;
import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.AccountDto;
import com.example.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> openAccount(@RequestBody AccountData accountData){
        AccountDto body = accountService.openAccount(accountData);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/updateBalance/{id}/{amount}")
    public ResponseEntity<AccountDto> updateBalance(@PathVariable("id") Long accountId, @PathVariable("amount") BigDecimal amount){
        AccountDto body = accountService.updateBalance(accountId,amount);
        return ResponseEntity.ok(body);
    }
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<AccountDto> changeStatus(@PathVariable("id") Long accountId, @RequestBody Status status){
        AccountDto body = accountService.changeStatus(accountId, status);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/close/{id}")
    public ResponseEntity<AccountDto> closeAccount(@PathVariable("id") Long id){
        AccountDto body = accountService.closeAccount(id);
        return ResponseEntity.ok(body);
    }

}
