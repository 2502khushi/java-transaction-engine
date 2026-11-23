package com.javaEng.javaEng.dto;

import java.math.BigDecimal;
import com.javaEng.javaEng.model.TransactionType;

public class TransactionRequest {
    private String transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private Long sourceAccount;
    private Long destinationAccount;

    public TransactionRequest(){

    }
    public TransactionRequest(String transactionId, TransactionType type, BigDecimal amount, Long sourceAccount,
            Long destinationAccount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
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
