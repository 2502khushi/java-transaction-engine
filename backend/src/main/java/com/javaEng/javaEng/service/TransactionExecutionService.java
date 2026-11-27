
package com.javaEng.javaEng.service;

import com.javaEng.javaEng.exception.AccountNotFoundException;
import com.javaEng.javaEng.exception.InsufficientBalanceException;
import com.javaEng.javaEng.exception.InvalidTransactionException;
import com.javaEng.javaEng.model.Account;
import com.javaEng.javaEng.repository.AccountRepository;
import com.javaEng.javaEng.repository.AuditRepository;
import com.javaEng.javaEng.repository.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionExecutionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuditRepository auditRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionExecutionService.class);

    public TransactionExecutionService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            AuditRepository auditRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.auditRepository = auditRepository;
    }

    @Transactional
    public void deposit(String transactionId, Long accountId, BigDecimal amount) {

        long startTime = System.currentTimeMillis();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new InvalidTransactionException("Account is not active");
        }

        int rowsUpdated = accountRepository.credit(accountId, amount);

        if (rowsUpdated == 0) {
            throw new InvalidTransactionException("Deposit failed");
        }

        transactionRepository.updateTransactionStatus(transactionId, "SUCCESS");

        long duration = System.currentTimeMillis() - startTime;

        logger.info(
                "transactionId={} accountId={} thread={} operation=DEPOSIT result=SUCCESS durationMs={}",
                transactionId, accountId, Thread.currentThread().getName(), duration
        );

        auditRepository.saveAudit(
                transactionId,
                Thread.currentThread().getName(),
                duration,
                "DEPOSIT",
                "SUCCESS",
                "Deposit completed successfully"
        );
    }

    @Transactional
    public void withdraw(String transactionId, Long accountId, BigDecimal amount) {

        long startTime = System.currentTimeMillis();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new InvalidTransactionException("Account is not active");
        }

        int rowsUpdated = accountRepository.debitIfSufficient(accountId, amount);

        if (rowsUpdated == 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        transactionRepository.updateTransactionStatus(transactionId, "SUCCESS");

        long duration = System.currentTimeMillis() - startTime;

        logger.info(
                "transactionId={} accountId={} thread={} operation=WITHDRAWAL result=SUCCESS durationMs={}",
                transactionId, accountId, Thread.currentThread().getName(), duration
        );

        auditRepository.saveAudit(
                transactionId,
                Thread.currentThread().getName(),
                duration,
                "WITHDRAWAL",
                "SUCCESS",
                "Withdrawal completed successfully"
        );
    }

    @Transactional
    public void transfer(String transactionId, Long sourceAccountId,
                         Long destinationAccountId, BigDecimal amount) {

        long startTime = System.currentTimeMillis();

        if (sourceAccountId.equals(destinationAccountId)) {
            throw new InvalidTransactionException(
                    "Source and destination accounts cannot be the same"
            );
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

        int debitRows = accountRepository.debitIfSufficient(sourceAccountId, amount);

        if (debitRows == 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        int creditRows = accountRepository.credit(destinationAccountId, amount);

        if (creditRows == 0) {
            throw new InvalidTransactionException("Transfer credit failed");
        }

        transactionRepository.updateTransactionStatus(transactionId, "SUCCESS");

        long duration = System.currentTimeMillis() - startTime;

        logger.info(
                "transactionId={} sourceAccountId={} destinationAccountId={} thread={} operation=TRANSFER result=SUCCESS durationMs={}",
                transactionId, sourceAccountId, destinationAccountId,
                Thread.currentThread().getName(), duration
        );

        auditRepository.saveAudit(
                transactionId,
                Thread.currentThread().getName(),
                duration,
                "TRANSFER",
                "SUCCESS",
                "Transfer completed successfully"
        );
    }
}
