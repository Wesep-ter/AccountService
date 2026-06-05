package com.example.accountservice.controller;

import com.example.accountservice.constant.Status;
import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.AccountDto;
import com.example.accountservice.dto.BalanceUpdateRequest;
import com.example.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long id) {
        AccountDto body = accountService.getAccountById(id);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<AccountDto> openAccount(@RequestBody AccountData accountData){
        AccountDto body = accountService.openAccount(accountData);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{id}/execution-balance")
    public ResponseEntity<AccountDto> updateBalance(
            @PathVariable("id") Long accountId,
            @RequestBody BalanceUpdateRequest request){
        AccountDto body = accountService.updateBalance(accountId, request.getAmount());
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
