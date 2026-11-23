package com.javaEng.javaEng.model;

import java.math.BigDecimal;

public class Account {
    private Long accountId;
    private Long customerId;
    private BigDecimal balance;
    private String status;

    public Account(){

    }
    public Account(Long accountId, Long customerId, BigDecimal balance, String status) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
        this.status = status;
    }


    
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
