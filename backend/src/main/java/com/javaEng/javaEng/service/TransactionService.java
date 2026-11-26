package com.javaEng.javaEng.service;

import com.javaEng.javaEng.exception.InvalidTransactionException;
import com.javaEng.javaEng.model.Account;
import com.javaEng.javaEng.repository.AccountRepository;
import com.javaEng.javaEng.repository.TransactionRepository;
import com.javaEng.javaEng.exception.AccountNotFoundException;
import com.javaEng.javaEng.exception.InsufficientBalanceException;


import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository,
                             TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidTransactionException("Deposit amount must be greater than zero");
    }

    Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found"));

    if (!"ACTIVE".equals(account.getStatus())) {
        throw new InvalidTransactionException("Account is not active");
    }

    accountRepository.credit(accountId, amount);
}
@Transactional
public void withdraw(Long accountId, BigDecimal amount) {

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidTransactionException("Withdrawal amount must be greater than zero");
    }

    Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found"));

    if (!"ACTIVE".equals(account.getStatus())) {
        throw new InvalidTransactionException("Account is not active");
    }

    int rowsUpdated = accountRepository.debitIfSufficient(accountId, amount);

    if (rowsUpdated == 0) {
        throw new InsufficientBalanceException("Insufficient balance");
    }
}
@Transactional
public void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidTransactionException("Transfer amount must be greater than zero");
    }

    if (sourceAccountId.equals(destinationAccountId)) {
        throw new InvalidTransactionException("Source and destination accounts cannot be the same");
    }

    Long firstAccountId = Math.min(sourceAccountId, destinationAccountId);
    Long secondAccountId = Math.max(sourceAccountId, destinationAccountId);

    Account firstAccount = accountRepository.findByIdForUpdate(firstAccountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found"));

    Account secondAccount = accountRepository.findByIdForUpdate(secondAccountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found"));

    Account sourceAccount = sourceAccountId.equals(firstAccountId)
            ? firstAccount : secondAccount;

    Account destinationAccount = destinationAccountId.equals(firstAccountId)
            ? firstAccount : secondAccount;

    if (!"ACTIVE".equals(sourceAccount.getStatus())) {
        throw new InvalidTransactionException("Source account is not active");
    }

    if (!"ACTIVE".equals(destinationAccount.getStatus())) {
        throw new InvalidTransactionException("Destination account is not active");
    }

    int rowsUpdated = accountRepository.debitIfSufficient(sourceAccountId, amount);

    if (rowsUpdated == 0) {
        throw new InsufficientBalanceException("Insufficient balance");
    }

    // accountRepository.credit(destinationAccountId, amount);
    int creditRows = accountRepository.credit(destinationAccountId, amount);

    if (creditRows == 0) {
        throw new InvalidTransactionException("Transfer credit failed");
    }
}
}