package com.mybank.atm.entity.json;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.math.BigDecimal;

/**
 * JSON Resource entity to represent a response to a user after a withdrawal.
 *
 * @author brian.e.reynolds@outlook.com
 */
@JsonRootName(value = "Withdrawal")
public class WithdrawalResource {

    private BigDecimal amount;
    private CashMapResource cashMap;
    private AccountResource accountResource;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CashMapResource getCashMap() {
        return cashMap;
    }

    public void setCashMap(CashMapResource cashMap) {
        this.cashMap = cashMap;
    }

    public AccountResource getAccountResource() {
        return accountResource;
    }

    public void setAccountResource(AccountResource accountResource) {
        this.accountResource = accountResource;
    }
}
