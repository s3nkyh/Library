package org.library.repo.jooq;

import com.example.jooq.tables.*;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BorrowingJooqRepo {
    private final DSLContext dsl;

    public Book findMostBorrowedBook() {
        Books b = Books.BOOKS;
        Borrowings br = Borrowings.BORROWINGS;
        Authors a = Authors.AUTHORS;
        Genres g = Genres.GENRES;

        return dsl.select(b.asterisk(), a.asterisk(), g.asterisk())
                .from(br)
                .join(b).on(br.BOOK_ID.eq(b.ID))
                .join(a).on(b.AUTHOR_ID.eq(a.ID))
                .join(g).on(b.GENRE_ID.eq(g.ID))
                .groupBy(b.ID, a.ID, g.ID)
                .orderBy(DSL.count().desc())
                .limit(1)
                .fetchOne()
                .map(this::mapToBookWithAuthorAndGenre);
    }

    public List<Book> findBooksByReaderId(Long readerId) {
        Books b = Books.BOOKS;
        Borrowings br = Borrowings.BORROWINGS;
        Readers r = Readers.READERS;
        Authors a = Authors.AUTHORS;
        Genres g = Genres.GENRES;

        return dsl.select(b.asterisk(), a.asterisk(), g.asterisk())
                .from(br)
                .join(b).on(br.BOOK_ID.eq(b.ID))
                .join(r).on(br.READER_ID.eq(r.ID))
                .join(a).on(b.AUTHOR_ID.eq(a.ID))
                .join(g).on(b.GENRE_ID.eq(g.ID))
                .where(r.ID.eq(readerId))
                .fetch(this::mapToBookWithAuthorAndGenre);
    }

    private Book mapToBookWithAuthorAndGenre(Record record) {
        Books b = Books.BOOKS;
        Authors a = Authors.AUTHORS;
        Genres g = Genres.GENRES;

        Author author = Author.builder()
                .id(record.get(a.ID))
                .name(record.get(a.NAME))
                .birthDate(record.get(a.BIRTH_DATE))
                .country(record.get(a.COUNTRY))
                .build();

        Genre genre = Genre.builder()
                .id(record.get(g.ID))
                .name(record.get(g.NAME))
                .build();

        return Book.builder()
                .id(record.get(b.ID))
                .title(record.get(b.TITLE))
                .author(author)
                .genre(genre)
                .publishedYear(record.get(b.PUBLISHED_YEAR))
                .availableCopies(record.get(b.AVAILABLE_COPIES))
                .totalCopies(record.get(b.TOTAL_COPIES))
                .build();
    }
}