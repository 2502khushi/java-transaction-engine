package com.javaEng.javaEng.dto;

import java.math.BigDecimal;

import com.javaEng.javaEng.model.TransactionStatus;

public class TransactionResponse {
    private String transactionId;
    private TransactionStatus status;
    private String message;
    private BigDecimal amount;

    
    public TransactionResponse(){

    }
    public TransactionResponse(String transactionId, TransactionStatus status, String message, BigDecimal amount) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.amount = amount;
    }

    
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public TransactionStatus getStatus() {
        return status;
    }
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    
}
