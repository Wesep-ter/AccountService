package com.example.accountservice.exception;

public class CloseAccountException extends RuntimeException {
    public CloseAccountException(String message) {
        super(message);
    }
}
