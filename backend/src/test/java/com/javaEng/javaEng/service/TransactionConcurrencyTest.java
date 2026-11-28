package com.javaEng.javaEng.service;

import com.javaEng.javaEng.repository.AccountRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionConcurrencyTest {

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
    void testConcurrentWithdrawals() throws InterruptedException, ExecutionException  {
                ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch startLatch = new CountDownLatch(1);
        String transactionIdA = "DAY6-TXN-A-" + UUID.randomUUID();
        String transactionIdB = "DAY6-TXN-B-" + UUID.randomUUID();
        
        Future<String> threadA = executor.submit(() -> {
            startLatch.await();
            try {
                transactionService.withdraw(
                 transactionIdA, 3L, new BigDecimal("7000")
                );
                return "SUCCESS";
            } catch (Exception ex) {
                return ex.getMessage();
            }
        });
        Future<String> threadB = executor.submit(() -> {
            startLatch.await();
            try {
                transactionService.withdraw(
                        transactionIdB, 3L, new BigDecimal("5000")
                );
                return "SUCCESS";
            } catch (Exception ex) {
                return ex.getMessage();
            }
        });
                startLatch.countDown();

        String resultA = threadA.get();
        String resultB = threadB.get();

        executor.shutdown();

        System.out.println("Thread A: " + resultA);
        System.out.println("Thread B: " + resultB);
                long successCount = 0;

        if ("SUCCESS".equals(resultA)) {
            successCount++;
        }

        if ("SUCCESS".equals(resultB)) {
            successCount++;
        }

        assertEquals(1, successCount);

        BigDecimal finalBalance = accountRepository.findById(3L)
                .orElseThrow()
                .getBalance();

        // assertEquals(new BigDecimal("3000.00"), finalBalance);
        assertTrue(
        finalBalance.equals(new BigDecimal("3000.00"))
        || finalBalance.equals(new BigDecimal("5000.00"))
        );
    }
}