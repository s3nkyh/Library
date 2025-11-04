package org.library.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.BaseIntegrationTest;
import org.library.repo.jdbc.AuthorsJdbcRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorsJdbcRepoTest extends BaseIntegrationTest {
    @Autowired
    private AuthorsJdbcRepo authorsJdbcRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("TRUNCATE borrowings, books, authors, genres, readers RESTART IDENTITY CASCADE");

        jdbcTemplate.execute("INSERT INTO authors (name, birth_date) VALUES ('A1', '1980-01-01')");
        jdbcTemplate.execute("INSERT INTO authors (name, birth_date) VALUES ('A2', '1980-05-10')");
        jdbcTemplate.execute("INSERT INTO authors (name, birth_date) VALUES ('A3', '1995-02-02')");
    }

    @Test
    void testCountAuthorsByBirthYear() {
        Map<Integer, Integer> result = authorsJdbcRepo.countAuthorsByBirthYear();

        assertThat(result).containsEntry(1980, 2);
        assertThat(result).containsEntry(1995, 1);
    }
}
