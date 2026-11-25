package com.javaEng.javaEng.controller;

import com.javaEng.javaEng.service.TransactionService;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping("/deposit")
        public String deposit(
        @RequestParam Long accountId,
        @RequestParam BigDecimal amount) {

    transactionService.deposit(accountId, amount);

    return "Deposit successful";
    }
    @PostMapping("/withdraw")
    public String withdraw(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount) {

        transactionService.withdraw(accountId, amount);

        return "Withdrawal successful";
    }
