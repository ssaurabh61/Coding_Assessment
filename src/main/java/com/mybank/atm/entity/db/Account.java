package com.mybank.atm.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Entity to represent a Bank Account
 *
 * @author brian.e.reynolds@outlook.com
 */
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false)
    private Long accountNumber;

    @Column(nullable = false)
    private String pin;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private BigDecimal overdraft;

    protected Account() {
    }

    public Account(Long accountNumber, String pin) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.overdraft = BigDecimal.valueOf(0);
        this.balance = BigDecimal.valueOf(0);
    }

    public Long getId() {
        return id;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.overdraft = overdraft;
    }
}
