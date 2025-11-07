package org.library.repo.jdbc;

import lombok.RequiredArgsConstructor;
import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BorrowingJdbcRepo {
    private final JdbcTemplate jdbcTemplate;

    public Book findMostBorrowedBook() {
        String sql = """
                    SELECT 
                        b.id AS book_id, 
                        b.title AS book_title, 
                        b.published_year AS book_year,
                        b.available_copies AS book_available,
                        b.total_copies AS book_total,
                        a.id AS author_id,
                        a.name AS author_name,
                        a.birth_date AS author_birth_date,
                        a.country AS author_country,
                        g.id AS genre_id,
                        g.name AS genre_name
                    FROM borrowings br
                    JOIN books b ON br.book_id = b.id
                    JOIN authors a ON b.author_id = a.id
                    JOIN genres g ON b.genre_id = g.id
                    GROUP BY b.id, a.id, g.id
                    ORDER BY COUNT(br.id) DESC
                    LIMIT 1
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return bookBuilder(rs);
        });
    }

    public List<Book> findBooksByReaderId(long readerId) {
        String sql = """
                    SELECT 
                        b.id AS book_id, 
                        b.title AS book_title, 
                        b.published_year AS book_year,
                        b.available_copies AS book_available,
                        b.total_copies AS book_total,
                        a.id AS author_id,
                        a.name AS author_name,
                        a.birth_date AS author_birth_date,
                        a.country AS author_country,
                        g.id AS genre_id,
                        g.name AS genre_name
                    FROM borrowings br
                    JOIN books b ON br.book_id = b.id
                    JOIN authors a ON b.author_id = a.id
                    JOIN genres g ON b.genre_id = g.id
                    WHERE br.reader_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return bookBuilder(rs);
        }, readerId);
    }

    private Book bookBuilder(ResultSet rs) {
        try {
            Author author = Author.builder()
                    .id(rs.getLong("author_id"))
                    .name(rs.getString("author_name"))
                    .birthDate(rs.getObject("author_birth_date", LocalDate.class))
                    .country(rs.getString("author_country"))
                    .build();

            Genre genre = Genre.builder()
                    .id(rs.getLong("genre_id"))
                    .name(rs.getString("genre_name"))
                    .build();

            return Book.builder()
                    .id(rs.getLong("book_id"))
                    .title(rs.getString("book_title"))
                    .publishedYear(rs.getInt("book_year"))
                    .availableCopies(rs.getInt("book_available"))
                    .totalCopies(rs.getInt("book_total"))
                    .author(author)
                    .genre(genre)
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong while trying to get the book builder");
        }
    }
}
