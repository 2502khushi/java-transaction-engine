
package com.javaEng.javaEng.repository;

import com.javaEng.javaEng.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Account mapAccount(ResultSet rs) throws SQLException {
        Account account = new Account();

        account.setAccountId(rs.getLong("account_id"));
        account.setCustomerId(rs.getLong("customer_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setStatus(rs.getString("status"));

        return account;
    }

    public Optional<Account> findById(Long accountId) {

        String sql = "SELECT * FROM account WHERE account_id = ?";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapAccount(rs),
                accountId
        ).stream().findFirst();
    }

    public Optional<Account> findByIdForUpdate(Long accountId) {

        String sql = "SELECT * FROM account WHERE account_id = ? FOR UPDATE";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapAccount(rs),
                accountId
        ).stream().findFirst();
    }

    public int debitIfSufficient(
            Long accountId,
            java.math.BigDecimal amount) {

        String sql = """
                UPDATE account
                SET balance = balance - ?
                WHERE account_id = ?
                AND balance >= ?
                AND status = 'ACTIVE'
                """;

        return jdbcTemplate.update(
                sql, amount, accountId, amount
        );
    }

    public int credit(
            Long accountId,
            java.math.BigDecimal amount) {

        String sql = """
                UPDATE account
                SET balance = balance + ?
                WHERE account_id = ?
                AND status = 'ACTIVE'
                """;

        return jdbcTemplate.update(sql, amount, accountId);
    }
}
