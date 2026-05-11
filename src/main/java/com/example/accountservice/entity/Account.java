package com.example.accountservice.entity;

import com.example.accountservice.constant.AccountType;
import com.example.accountservice.constant.Currency;
import com.example.accountservice.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "accounts")
@Builder
@DynamicInsert
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

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal balance;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "status")
    private Status status;


}
