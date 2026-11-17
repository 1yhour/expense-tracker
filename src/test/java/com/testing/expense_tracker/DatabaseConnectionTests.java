package com.testing.expense_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifies the application can connect to PostgreSQL and use the scaffold
 * "test" table created from {@code com.testing.expense_tracker.entity.Test}.
 */
@SpringBootTest
@Transactional
class DatabaseConnectionTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void writesAndReadsTestTable() {
        jdbcTemplate.update("insert into test (name, age, address) values (?, ?, ?)",
                "Connection Check", 1, "N/A");

        Integer rows = jdbcTemplate.queryForObject(
                "select count(*) from test where name = ?", Integer.class, "Connection Check");

        assertEquals(1, rows);
    }
}
