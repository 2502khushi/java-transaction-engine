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
    // @PostMapping("/deposit")
    //     public String deposit(
    //     @RequestParam Long accountId,
    //     @RequestParam BigDecimal amount) {

    // transactionService.deposit(accountId, amount);

    // return "Deposit successful";
    // }
    @PostMapping("/deposit")
public void deposit(
        @RequestParam String transactionId,
        @RequestParam Long accountId,
        @RequestParam BigDecimal amount) {

    transactionService.deposit(transactionId, accountId, amount);
}
    // @PostMapping("/withdraw")
    // public String withdraw(
    //         @RequestParam Long accountId,
    //         @RequestParam BigDecimal amount) {

    //     transactionService.withdraw(accountId, amount);

    //     return "Withdrawal successful";
    // }
    @PostMapping("/withdraw")
    public void withdraw(
            @RequestParam String transactionId,
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount) {

        transactionService.withdraw(transactionId, accountId, amount);
    }
    // @PostMapping("/transfer")
    // public String transfer(
    //     @RequestParam Long sourceAccountId,
    //     @RequestParam Long destinationAccountId,
    //     @RequestParam BigDecimal amount) {

    // transactionService.transfer(sourceAccountId, destinationAccountId, amount);

    // return "Transfer successful";
    // }

    @PostMapping("/transfer")
public void transfer(
        @RequestParam String transactionId,
        @RequestParam Long sourceAccountId,
        @RequestParam Long destinationAccountId,
        @RequestParam BigDecimal amount) {

    transactionService.transfer(
            transactionId,
            sourceAccountId,
            destinationAccountId,
            amount
    );
}
}