package com.example.accountservice.exception;

public class BlockedAccountException extends RuntimeException {
    public BlockedAccountException(String message) {
        super(message);
    }
}
