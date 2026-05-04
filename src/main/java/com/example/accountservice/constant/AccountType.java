package com.example.accountservice.constant;

import lombok.Getter;

@Getter
public enum AccountType {
    CHECKING("Текущий/Расчётный", 40817),
    SAVINGS ("Сберегательный",    42301),
    DEPOSIT ("Депозитный/Вклад",  42601),
    CREDIT  ("Кредитный",         45502);

    private final String description;
    private final int balanceCode;

    AccountType(String description, int balanceCode) {
        this.description = description;
        this.balanceCode = balanceCode;
    }
}
