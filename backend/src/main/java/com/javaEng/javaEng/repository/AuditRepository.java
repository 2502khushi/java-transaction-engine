
package com.javaEng.javaEng.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuditRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuditRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveAudit(
            String transactionId,
            String threadName,
            long durationMs,
            String operation,
            String result,
            String details) {

        String sql = """
                INSERT INTO transaction_audit
                (transaction_id, thread_name, duration_ms,
                 operation, result, details)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                transactionId,
                threadName,
                durationMs,
                operation,
                result,
                details
        );
    }
}
