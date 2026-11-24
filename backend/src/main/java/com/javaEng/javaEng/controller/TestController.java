
package com.javaEng.javaEng.controller;

import com.javaEng.javaEng.model.Account;
import com.javaEng.javaEng.model.Customer;
import com.javaEng.javaEng.model.Transaction;
import com.javaEng.javaEng.repository.AccountRepository;
import com.javaEng.javaEng.repository.CustomerRepository;
import com.javaEng.javaEng.repository.DatabaseTest;
import com.javaEng.javaEng.repository.FailedTransactionRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javaEng.javaEng.repository.CustomerRepository;
import com.javaEng.javaEng.model.Customer;
import com.javaEng.javaEng.model.Transaction;
import com.javaEng.javaEng.repository.TransactionRepository;
import com.javaEng.javaEng.repository.AuditRepository;
import com.javaEng.javaEng.repository.FailedTransactionRepository;

@RestController
public class TestController {

    private final DatabaseTest databaseTest;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final AuditRepository auditRepository;
    private final FailedTransactionRepository failedTransactionRepository;
    
    public TestController(
            DatabaseTest databaseTest,
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            TransactionRepository transactionRepository,
            AuditRepository auditRepository,
            FailedTransactionRepository failedTransactionRepository) {

        this.databaseTest = databaseTest;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository=transactionRepository;
        this.auditRepository = auditRepository;
        this.failedTransactionRepository = failedTransactionRepository;
    }

    @GetMapping("/test-db")
    public String testDatabase() {
        return "Connected to database: "
                + databaseTest.checkConnection();
    }

    @GetMapping("/test-account")
    public Account testAccount() {
        return accountRepository.findById(1L).orElse(null);
    }
    @GetMapping("/test-customer")
    public Customer testCustomer() {
    return customerRepository.findById(1L).orElse(null);
    }
    @GetMapping("/test-transaction")
    public Transaction testTransaction() {
        return transactionRepository.findById("TXN001").orElse(null);
    }
    @GetMapping("/test-audit")
    public String testAudit() {

        auditRepository.saveAudit(
                "TXN001",
                Thread.currentThread().getName(),
                25,
                "DEPOSIT",
                "SUCCESS",
                "Test audit record"
        );

        return "Audit record saved successfully!";
    }
    @GetMapping("/test-failure")
public String testFailure() {

    failedTransactionRepository.saveFailure(
            "TXN001",
            "TEST_ERROR",
            "Testing failed transaction repository"
    );

    return "Failure record saved successfully!";
}
}
