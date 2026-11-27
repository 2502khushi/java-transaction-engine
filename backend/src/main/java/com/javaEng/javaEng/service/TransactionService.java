// package com.javaEng.javaEng.service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import com.javaEng.javaEng.exception.InvalidTransactionException;
// import com.javaEng.javaEng.model.Account;
// import com.javaEng.javaEng.repository.AccountRepository;
// import com.javaEng.javaEng.repository.TransactionRepository;
// import com.javaEng.javaEng.exception.AccountNotFoundException;
// import com.javaEng.javaEng.exception.InsufficientBalanceException;
// import com.javaEng.javaEng.repository.AuditRepository;
// import com.javaEng.javaEng.service.FailureRecordingService;
// import com.javaEng.javaEng.service.TransactionRecordService;

// import org.springframework.transaction.annotation.Transactional;

// import java.math.BigDecimal;

// import org.springframework.stereotype.Service;

// @Service
// public class TransactionService {

//     private final AccountRepository accountRepository;
//     private final TransactionRepository transactionRepository;
//     private final AuditRepository auditRepository;
//     private final FailureRecordingService failureRecordingService;
//     private final TransactionRecordService transactionRecordService;

//     public TransactionService(
//         AccountRepository accountRepository,
//         TransactionRepository transactionRepository,
//         AuditRepository auditRepository,
//         FailureRecordingService failureRecordingService,
//         TransactionRecordService transactionRecordService) {

//     this.accountRepository = accountRepository;
//     this.transactionRepository = transactionRepository;
//     this.auditRepository = auditRepository;
//     this.failureRecordingService = failureRecordingService;
//     this.transactionRecordService = transactionRecordService;
//     }
// //     @Transactional
// //     public void deposit(Long accountId, BigDecimal amount) {

// //     if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
// //         throw new InvalidTransactionException("Deposit amount must be greater than zero");
// //     }

// //     Account account = accountRepository.findById(accountId)
// //             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

// //     if (!"ACTIVE".equals(account.getStatus())) {
// //         throw new InvalidTransactionException("Account is not active");
// //     }

// //     accountRepository.credit(accountId, amount);
// // }

// @Transactional
// public void deposit(String transactionId, Long accountId, BigDecimal amount) {
//     long startTime = System.currentTimeMillis();
//     if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
//         throw new InvalidTransactionException("Deposit amount must be greater than zero");
//     }

//     transactionRepository.savePendingTransaction(
//             transactionId,
//             "DEPOSIT",
//             amount,
//             null,
//             accountId
//     );

//     Account account = accountRepository.findById(accountId)
//             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

//     if (!"ACTIVE".equals(account.getStatus())) {
//         throw new InvalidTransactionException("Account is not active");
//     }

//     int rowsUpdated = accountRepository.credit(accountId, amount);

//     if (rowsUpdated == 0) {
//         throw new InvalidTransactionException("Deposit failed");
//     }

//     transactionRepository.updateTransactionStatus(transactionId, "SUCCESS");
//     logger.info("transactionId={} accountId={} thread={} operation=DEPOSIT result=SUCCESS durationMs={}",
//         transactionId, accountId, Thread.currentThread().getName(),
//         System.currentTimeMillis() - startTime);

//         auditRepository.saveAudit(
//         transactionId,
//         Thread.currentThread().getName(),
//         System.currentTimeMillis() - startTime,
//         "DEPOSIT",
//         "SUCCESS",
//         "Deposit completed successfully"
//         );
// }
// // @Transactional
// // public void withdraw(Long accountId, BigDecimal amount) {

// //     if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
// //         throw new InvalidTransactionException("Withdrawal amount must be greater than zero");
// //     }

// //     Account account = accountRepository.findById(accountId)
// //             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

// //     if (!"ACTIVE".equals(account.getStatus())) {
// //         throw new InvalidTransactionException("Account is not active");
// //     }

// //     int rowsUpdated = accountRepository.debitIfSufficient(accountId, amount);

// //     if (rowsUpdated == 0) {
// //         throw new InsufficientBalanceException("Insufficient balance");
// //     }
// // }
// @Transactional
// public void withdraw(String transactionId, Long accountId, BigDecimal amount) {
//     long startTime = System.currentTimeMillis();
//     if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
//         throw new InvalidTransactionException("Withdrawal amount must be greater than zero");
//     }

//     transactionRepository.savePendingTransaction(
//             transactionId,
//             "WITHDRAWAL",
//             amount,
//             accountId,
//             null
//     );

//     Account account = accountRepository.findById(accountId)
//             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

//     if (!"ACTIVE".equals(account.getStatus())) {
//         throw new InvalidTransactionException("Account is not active");
//     }

//     int rowsUpdated = accountRepository.debitIfSufficient(accountId, amount);

//     if (rowsUpdated == 0) {
//         throw new InsufficientBalanceException("Insufficient balance");
//     }

//     transactionRepository.updateTransactionStatus(transactionId, "SUCCESS");
//     logger.info("transactionId={} accountId={} thread={} operation=WITHDRAWAL result=SUCCESS durationMs={}",
//         transactionId, accountId, Thread.currentThread().getName(),
//         System.currentTimeMillis() - startTime);

//         auditRepository.saveAudit(
//         transactionId,
//         Thread.currentThread().getName(),
//         System.currentTimeMillis() - startTime,
//         "WITHDRAWAL",
//         "SUCCESS",
//         "Withdrawal completed successfully"
//     );
// }
// // @Transactional
// // public void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {

// //     if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
// //         throw new InvalidTransactionException("Transfer amount must be greater than zero");
// //     }

// //     if (sourceAccountId.equals(destinationAccountId)) {
// //         throw new InvalidTransactionException("Source and destination accounts cannot be the same");
// //     }

// //     Long firstAccountId = Math.min(sourceAccountId, destinationAccountId);
// //     Long secondAccountId = Math.max(sourceAccountId, destinationAccountId);

// //     Account firstAccount = accountRepository.findByIdForUpdate(firstAccountId)
// //             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

// //     Account secondAccount = accountRepository.findByIdForUpdate(secondAccountId)
// //             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

// //     Account sourceAccount = sourceAccountId.equals(firstAccountId)
// //             ? firstAccount : secondAccount;

// //     Account destinationAccount = destinationAccountId.equals(firstAccountId)
// //             ? firstAccount : secondAccount;

// //     if (!"ACTIVE".equals(sourceAccount.getStatus())) {
// //         throw new InvalidTransactionException("Source account is not active");
// //     }

// //     if (!"ACTIVE".equals(destinationAccount.getStatus())) {
// //         throw new InvalidTransactionException("Destination account is not active");
// //     }

// //     int rowsUpdated = accountRepository.debitIfSufficient(sourceAccountId, amount);

// //     if (rowsUpdated == 0) {
// //         throw new InsufficientBalanceException("Insufficient balance");
// //     }

// //     // accountRepository.credit(destinationAccountId, amount);
// //     int creditRows = accountRepository.credit(destinationAccountId, amount);

// //     if (creditRows == 0) {
// //         throw new InvalidTransactionException("Transfer credit failed");
// //     }
// // }


// @Transactional
// public void transfer(String transactionId, Long sourceAccountId,
//                      Long destinationAccountId, BigDecimal amount) {

//     long startTime = System.currentTimeMillis();
//     if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
//         throw new InvalidTransactionException("Transfer amount must be greater than zero");
//     }

//     if (sourceAccountId.equals(destinationAccountId)) {
//         throw new InvalidTransactionException("Source and destination accounts cannot be the same");
//     }

//     transactionRepository.savePendingTransaction(
//             transactionId,
//             "TRANSFER",
//             amount,
//             sourceAccountId,
//             destinationAccountId
//     );

//     Long firstAccountId = Math.min(sourceAccountId, destinationAccountId);
//     Long secondAccountId = Math.max(sourceAccountId, destinationAccountId);

//     Account firstAccount = accountRepository.findByIdForUpdate(firstAccountId)
//             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

//     Account secondAccount = accountRepository.findByIdForUpdate(secondAccountId)
//             .orElseThrow(() -> new AccountNotFoundException("Account not found"));

//     Account sourceAccount = sourceAccountId.equals(firstAccountId)
//             ? firstAccount : secondAccount;

//     Account destinationAccount = destinationAccountId.equals(firstAccountId)
//             ? firstAccount : secondAccount;

//     if (!"ACTIVE".equals(sourceAccount.getStatus())) {
//         throw new InvalidTransactionException("Source account is not active");
//     }

//     if (!"ACTIVE".equals(destinationAccount.getStatus())) {
//         throw new InvalidTransactionException("Destination account is not active");
//     }

//     int debitRows = accountRepository.debitIfSufficient(sourceAccountId, amount);

//     if (debitRows == 0) {
//         throw new InsufficientBalanceException("Insufficient balance");
//     }

//     int creditRows = accountRepository.credit(destinationAccountId, amount);

//     if (creditRows == 0) {
//         throw new InvalidTransactionException("Transfer credit failed");
//     }

//     transactionRepository.updateTransactionStatus(transactionId, "SUCCESS");
//     logger.info("transactionId={} sourceAccountId={} destinationAccountId={} thread={} operation=TRANSFER result=SUCCESS durationMs={}",
//         transactionId, sourceAccountId, destinationAccountId,
//         Thread.currentThread().getName(),
//         System.currentTimeMillis() - startTime);

//         auditRepository.saveAudit(
//         transactionId,
//         Thread.currentThread().getName(),
//         System.currentTimeMillis() - startTime,
//         "TRANSFER",
//         "SUCCESS",
//         "Transfer completed successfully"
// );
// }
// private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
// }




package com.javaEng.javaEng.service;

import com.javaEng.javaEng.exception.InsufficientBalanceException;
import com.javaEng.javaEng.exception.InvalidTransactionException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRecordService transactionRecordService;
    private final TransactionExecutionService transactionExecutionService;

    public TransactionService(
            TransactionRecordService transactionRecordService,
            TransactionExecutionService transactionExecutionService) {

        this.transactionRecordService = transactionRecordService;
        this.transactionExecutionService = transactionExecutionService;
    }

    public void deposit(String transactionId, Long accountId, BigDecimal amount) {

        validateAmount(amount);

        transactionRecordService.createPendingTransaction(
                transactionId, "DEPOSIT", amount, null, accountId
        );

        try {
            transactionExecutionService.deposit(transactionId, accountId, amount);
        } catch (RuntimeException ex) {
            recordFailure(transactionId, ex);
            throw ex;
        }
    }

    public void withdraw(String transactionId, Long accountId, BigDecimal amount) {

        validateAmount(amount);

        transactionRecordService.createPendingTransaction(
                transactionId, "WITHDRAWAL", amount, accountId, null
        );

        try {
            transactionExecutionService.withdraw(transactionId, accountId, amount);
        } catch (RuntimeException ex) {
            recordFailure(transactionId, ex);
            throw ex;
        }
    }

    public void transfer(String transactionId, Long sourceAccountId,
                        Long destinationAccountId, BigDecimal amount) {

        validateAmount(amount);

        if (sourceAccountId.equals(destinationAccountId)) {
            throw new InvalidTransactionException(
                    "Source and destination accounts cannot be the same"
            );
        }

        transactionRecordService.createPendingTransaction(
                transactionId, "TRANSFER", amount,
                sourceAccountId, destinationAccountId
        );

        try {
            transactionExecutionService.transfer(
                    transactionId, sourceAccountId, destinationAccountId, amount
            );
        } catch (RuntimeException ex) {
            recordFailure(transactionId, ex);
            throw ex;
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException(
                    "Transaction amount must be greater than zero"
            );
        }
    }

    private void recordFailure(String transactionId, RuntimeException ex) {

        String status = "REJECTED";
        String category = "BUSINESS_VALIDATION";

        if (!(ex instanceof InvalidTransactionException
                || ex instanceof InsufficientBalanceException
                || ex instanceof com.javaEng.javaEng.exception.AccountNotFoundException)) {

            status = "FAILED";
            category = "SYSTEM_ERROR";
        }

        transactionRecordService.markFailure(
                transactionId,
                status,
                category,
                ex.getMessage()
        );
    }
}