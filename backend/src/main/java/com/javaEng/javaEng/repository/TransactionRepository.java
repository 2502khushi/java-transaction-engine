
package com.javaEng.javaEng.repository;

import com.javaEng.javaEng.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
