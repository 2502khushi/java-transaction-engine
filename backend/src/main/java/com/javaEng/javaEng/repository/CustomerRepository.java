
package com.javaEng.javaEng.repository;

import com.javaEng.javaEng.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Customer> findById(Long customerId) {

        String sql = "SELECT * FROM customer WHERE customer_id = ?";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Customer customer = new Customer();

                    customer.setCustomerId(rs.getLong("customer_id"));
                    customer.setName(rs.getString("name"));
                    customer.setContact(rs.getString("contact"));

                    return customer;
                },
                customerId
        ).stream().findFirst();
    }
}
