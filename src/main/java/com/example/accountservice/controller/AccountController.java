package com.example.accountservice.controller;

import com.example.accountservice.dto.AccountData;
import com.example.accountservice.dto.CreateAccountDto;
import com.example.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<CreateAccountDto> openAccount(@RequestBody AccountData accountData){
        CreateAccountDto body = accountService.openAccount(accountData);
        return ResponseEntity.ok(body);
    }

}
