package com.example.accountservice.constant;

import lombok.Getter;

@Getter
public enum Currency {
    RUB("810"),
    USD("840"),
    EUR("978");

    private final String isoCode;

    Currency(String isoCode) {
        this.isoCode = isoCode;
    }
}