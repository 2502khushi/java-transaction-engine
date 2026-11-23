
package com.javaEng.javaEng.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseTest {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String checkConnection() {
        return jdbcTemplate.queryForObject(
            "SELECT DATABASE()",
            String.class
        );
    }
}
