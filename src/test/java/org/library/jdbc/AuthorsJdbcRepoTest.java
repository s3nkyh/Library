package org.library.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.BaseIntegrationTest;
import org.library.repo.jdbc.AuthorsJdbcRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = "spring.profiles.active=test")
public class AuthorsJdbcRepoTest {
    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18.0")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("password");

    @DynamicPropertySource
    static void registerPgContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

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
