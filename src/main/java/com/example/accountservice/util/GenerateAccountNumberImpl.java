package com.example.accountservice.util;

import com.example.accountservice.dto.AccountData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenerateAccountNumberImpl implements GenerateAccountNumber {

    private static final int[] WEIGHTS = {
            7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1
    };
    private static final Random RANDOM = new Random();


    @Value("${bank.bik-branch}")
    private String bikBranch;

    @Override
    public String generate(AccountData accountData){
        String balanceCode = String.valueOf(accountData.getAccountType().getBalanceCode());
        String currencyCode = accountData.getCurrency().getIsoCode();
        String personalAccount = String.format("%07d", RANDOM.nextInt(10_000_000));
        String rawAccount = balanceCode + currencyCode + "0" + bikBranch + personalAccount;
        int key = calculateKey(rawAccount);

        return balanceCode + currencyCode + key + bikBranch + personalAccount;
    }

    private int calculateKey(String rawAccount){
        String forCalculation = bikBranch.substring(bikBranch.length() - 3) + rawAccount;
        int sum = 0;
        for (int i = 0; i < 23; i++) {
            sum += WEIGHTS[i] * (forCalculation.charAt(i) - '0');
        }
        return (sum % 10) % 10;
    }
}
