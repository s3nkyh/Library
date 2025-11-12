package org.library.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.model.*;
import org.library.repo.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
public class BorrowingJpaRepoTest {
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BorrowingRepo borrowingRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private ReaderRepo readerRepo;

    @Autowired
    private GenreRepo genreRepo;

    @BeforeEach
    void setUp() {
        borrowingRepo.deleteAll();
        bookRepo.deleteAll();
        authorRepo.deleteAll();
        readerRepo.deleteAll();
        genreRepo.deleteAll();

        Author author = authorRepo.save(Author.builder()
                .name("Author 1")
                .birthDate(LocalDate.of(1980, 1, 1))
                .country("RU")
                .build());

        Genre genre = genreRepo.save(Genre.builder()
                .name("Genre")
                .build());

        Book book1 = bookRepo.save(Book.builder()
                .title("Book 1")
                .author(author)
                .genre(genre)
                .publishedYear(2000)
                .availableCopies(5)
                .totalCopies(10)
                .build());

        Book book2 = bookRepo.save(Book.builder()
                .title("Book 2")
                .genre(genre)
                .author(author)
                .publishedYear(2005)
                .availableCopies(5)
                .totalCopies(10)
                .build());

        Reader reader = readerRepo.save(Reader.builder()
                .name("Reader 1")
                .email("reader1@mail.com")
                .membershipDate(LocalDate.now())
                .isActive(true)
                .build());

        String sql = "INSERT INTO borrowings (book_id, reader_id, borrowed_date, due_date, status) VALUES (?, ?, ?, ?, ?::borrowing_status)";

        jdbcTemplate.update(sql, book1.getId(), reader.getId(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10), "BORROWED");
        jdbcTemplate.update(sql, book1.getId(), reader.getId(), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 10), "BORROWED");
        jdbcTemplate.update(sql, book2.getId(), reader.getId(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 10), "BORROWED");
    }

    @Test
    void testFindMostBorrowedBook() {
        Book mostBorrowed = borrowingRepo.findMostBorrowedBook();

        assertThat(mostBorrowed.getTitle()).isEqualTo("Book 1");
        assertThat(mostBorrowed.getAuthor().getName()).isEqualTo("Author 1");
    }

    @Test
    void testFindBooksByReaderId() {
        Reader reader = readerRepo.findAll().get(0);
        List<Borrowing> borrowings = borrowingRepo.findByReaderId(reader.getId());
        assertThat(borrowings).hasSize(3);
    }
}
