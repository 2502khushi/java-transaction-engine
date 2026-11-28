package com.javaEng.javaEng.service;

import com.javaEng.javaEng.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionRollbackTest {

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
    void testRollbackAfterDatabaseFailure() {

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS force_transaction_failure");

        jdbcTemplate.execute("""
                CREATE TRIGGER force_transaction_failure
                BEFORE UPDATE ON transaction_record
                FOR EACH ROW
                BEGIN
                    IF NEW.status = 'SUCCESS' THEN
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT = 'Forced database failure';
                    END IF;
                END
                """);

        try {
            assertThrows(Exception.class, () ->
                    transactionService.withdraw(
                            "DAY6-ROLLBACK-TEST",
                            3L,
                            new BigDecimal("2000")
                    )
            );

            BigDecimal balance = accountRepository.findById(3L)
                    .orElseThrow()
                    .getBalance();
            System.out.println("Balance after rollback: " + balance);

            assertEquals(
                    new BigDecimal("10000.00"),
                    balance
            );

        } finally {
            jdbcTemplate.execute(
                    "DROP TRIGGER IF EXISTS force_transaction_failure"
            );
        }
    }
}