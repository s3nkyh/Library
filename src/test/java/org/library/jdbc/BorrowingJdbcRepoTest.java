package org.library.jdbc;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.BaseIntegrationTest;
import org.library.model.Book;
import org.library.repo.jdbc.BorrowingJdbcRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
public class BorrowingJdbcRepoTest {
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
    private BorrowingJdbcRepo borrowingJdbcRepo;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("TRUNCATE borrowings, books, authors, genres, readers RESTART IDENTITY CASCADE");

        jdbcTemplate.update("INSERT INTO authors (name, birth_date, country) VALUES (?, ?, ?)",
                "J.R.R. Tolkien", LocalDate.of(1892, 1, 3), "United Kingdom");

        jdbcTemplate.update("INSERT INTO genres (name) VALUES (?)", "Fantasy");

        jdbcTemplate.update("""
                        INSERT INTO books (title, author_id, genre_id, published_year, available_copies, total_copies)
                        VALUES (?, ?, ?, ?, ?, ?)
                        """,
                "The Hobbit", 1L, 1L, 1937, 5, 5);

        jdbcTemplate.update("INSERT INTO readers (name, email, phone, membership_date, is_active) VALUES (?, ?, ?, ?, ?)",
                "Sam Reader", "sam@example.com", "+70000000000", LocalDate.now(), true);

        jdbcTemplate.update("INSERT INTO borrowings (book_id, reader_id, borrowed_date, due_date, status) VALUES (?, ?, ?, ?, ?::borrowing_status)",
                1L, 1L, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 10), "BORROWED");
        jdbcTemplate.update("INSERT INTO borrowings (book_id, reader_id, borrowed_date, due_date, status) VALUES (?, ?, ?, ?, ?::borrowing_status)",
                1L, 1L, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10), "RETURNED");
    }

    @Test
    void testFindMostBorrowedBook() {
        Book book = borrowingJdbcRepo.findMostBorrowedBook();

        assertThat(book).isNotNull();
        assertThat(book.getTitle()).isEqualTo("The Hobbit");
    }

    @Test
    void testFindBooksByReaderId() {
        List<Book> books = borrowingJdbcRepo.findBooksByReaderId(1L);
        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).isEqualTo("The Hobbit");
    }
}
