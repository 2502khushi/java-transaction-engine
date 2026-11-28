package com.javaEng.javaEng.service;

import com.javaEng.javaEng.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConcurrentTransferTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetAccounts() {
        jdbcTemplate.update(
                "UPDATE account SET balance = 10000.00 WHERE account_id IN (4, 5)"
        );
    }

    @Test
    void testConcurrentTransfers() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);

        String transactionIdA = "DAY6-TRANSFER-A-" + UUID.randomUUID();
        String transactionIdB = "DAY6-TRANSFER-B-" + UUID.randomUUID();

        Future<String> threadA = executor.submit(() -> {
            startLatch.await();
            try {
                transactionService.transfer(
                        transactionIdA, 4L, 5L, new BigDecimal("2000")
                );
                return "SUCCESS";
            } catch (Exception ex) {
                return ex.getMessage();
            }
        });

        Future<String> threadB = executor.submit(() -> {
            startLatch.await();
            try {
                transactionService.transfer(
                        transactionIdB, 5L, 4L, new BigDecimal("3000")
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

        assertEquals("SUCCESS", resultA);
        assertEquals("SUCCESS", resultB);

        BigDecimal balance4 = accountRepository.findById(4L)
                .orElseThrow()
                .getBalance();

        BigDecimal balance5 = accountRepository.findById(5L)
                .orElseThrow()
                .getBalance();

        assertEquals(
                new BigDecimal("20000.00"),
                balance4.add(balance5)
        );
    }
}