package com.testing.expense_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifies the application can reach PostgreSQL and use the schema generated
 * from the entities. Runs in a transaction that is rolled back, so no rows leak.
 */
@SpringBootTest
@Transactional
class DatabaseConnectionTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void writesAndReadsUsersTable() {
        String email = "connection-check-" + UUID.randomUUID() + "@example.com";

        jdbcTemplate.update(
                "insert into users (name, email, password, created_at, updated_at) values (?, ?, ?, now(), now())",
                "Connection Check", email, "not-a-real-hash");

        Integer rows = jdbcTemplate.queryForObject(
                "select count(*) from users where email = ?", Integer.class, email);

        assertEquals(1, rows);
    }
}
