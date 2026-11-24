
package com.javaEng.javaEng.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FailedTransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public FailedTransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveFailure(
            String transactionId,
            String errorCategory,
            String reason) {

        String sql = """
                INSERT INTO failed_transaction
                (transaction_id, error_category, reason)
                VALUES (?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                transactionId,
                errorCategory,
                reason
        );
    }
}
