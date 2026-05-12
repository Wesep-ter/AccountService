package com.example.accountservice.handler;

import com.example.accountservice.exception.AccountNotFoundException;
import com.example.accountservice.exception.BlockedAccountException;
import com.example.accountservice.exception.CloseAccountException;
import com.example.accountservice.exception.NotEnoughMoneyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFoundEx(AccountNotFoundException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BlockedAccountException.class)
    public ResponseEntity<?> blockedAccountEx(BlockedAccountException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CloseAccountException.class)
    public ResponseEntity<?> closeAccountEx(CloseAccountException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<?> notEnoughMoneyEx(NotEnoughMoneyException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
