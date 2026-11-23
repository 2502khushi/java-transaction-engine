package com.javaEng.javaEng.model;

import java.math.BigDecimal;

public class Transaction {
    private String transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private TransactionStatus status;
    private Long sourceAccount;
    private Long destinationAccount;
    public Transaction(){

    }
    public Transaction(String transactionId, TransactionType type, BigDecimal amount, TransactionStatus status,
            Long sourceAccount, Long destinationAccount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }


    
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public TransactionStatus getStatus() {
        return status;
    }
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    public Long getSourceAccount() {
        return sourceAccount;
    }
    public void setSourceAccount(Long sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    public Long getDestinationAccount() {
        return destinationAccount;
    }
    public void setDestinationAccount(Long destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    

    
}
