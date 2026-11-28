package com.javaEng.javaEng.service;

import java.util.UUID;

import com.javaEng.javaEng.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class IdempotencyTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetAccount() {
        jdbcTemplate.update(
                "UPDATE account SET balance = 10000.00 WHERE account_id = 3"
        );
    }

    @Test
    void duplicateTransactionMustNotDebitTwice() {

        String transactionId = "DAY6-IDEMPOTENCY-" + UUID.randomUUID();

        transactionService.withdraw(
                transactionId, 3L, new BigDecimal("2000")
        );

        assertThrows(Exception.class, () ->
                transactionService.withdraw(
                        transactionId, 3L, new BigDecimal("2000")
                )
        );

        BigDecimal finalBalance = accountRepository.findById(3L)
                .orElseThrow()
                .getBalance();

        assertEquals(
                new BigDecimal("8000.00"),
                finalBalance
        );
    }
}