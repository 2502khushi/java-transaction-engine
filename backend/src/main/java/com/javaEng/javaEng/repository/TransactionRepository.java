
package com.javaEng.javaEng.repository;

import com.javaEng.javaEng.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Transaction> findById(String transactionId) {

        String sql = """
                SELECT * FROM transaction_record
                WHERE transaction_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Transaction transaction = new Transaction();

                    transaction.setTransactionId(
                            rs.getString("transaction_id"));

                    transaction.setType(
                            com.javaEng.javaEng.model.TransactionType.valueOf(
                                    rs.getString("type")));

                    transaction.setAmount(
                            rs.getBigDecimal("amount"));

                    transaction.setStatus(
                            com.javaEng.javaEng.model.TransactionStatus.valueOf(
                                    rs.getString("status")));

                    transaction.setSourceAccount(
                            rs.getObject("source_account", Long.class));

                    transaction.setDestinationAccount(
                            rs.getObject("destination_account", Long.class));

                    return transaction;
                },
                transactionId
        ).stream().findFirst();
    }
    public int savePendingTransaction(
        String transactionId,
        String type,
        BigDecimal amount,
        Long sourceAccount,
        Long destinationAccount) {

    String sql = """
            INSERT INTO transaction_record
            (transaction_id, type, amount, status, source_account, destination_account)
            VALUES (?, ?, ?, 'PENDING', ?, ?)
            """;

    return jdbcTemplate.update(sql, transactionId, type, amount,
            sourceAccount, destinationAccount);
        }
        public int updateTransactionStatus(String transactionId, String status) {

        String sql = """
                UPDATE transaction_record
                SET status = ?, completed_at = CURRENT_TIMESTAMP
                WHERE transaction_id = ?
                """;

        return jdbcTemplate.update(sql, status, transactionId);
        }
}
