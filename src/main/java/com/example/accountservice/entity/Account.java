package com.example.accountservice.entity;

import com.example.accountservice.constant.AccountType;
import com.example.accountservice.constant.Currency;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "accounts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "balance",columnDefinition = "DECIMAL(19,2) DEFAULT 0.00", nullable = false)
    private BigDecimal balance;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "open_date")
    private LocalDate openDate;

}
