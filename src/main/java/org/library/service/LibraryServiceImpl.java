package org.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.library.dto.BookDto;
import org.library.mapper.BookMapper;
import org.library.model.Book;
import org.library.repo.jdbc.AuthorsJdbcRepo;
import org.library.repo.jdbc.BorrowingJdbcRepo;
import org.library.repo.jooq.AuthorJooqRepo;
import org.library.repo.jooq.BorrowingJooqRepo;
import org.library.repo.jpa.AuthorRepo;
import org.library.repo.jpa.BorrowingRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final BookMapper bookMapper;
    private final BorrowingRepo borrowingRepo;
    private final AuthorRepo authorRepo;
    private final BorrowingJooqRepo borrowingJooqRepo;
    private final AuthorJooqRepo authorJooqRepo;
    private final BorrowingJdbcRepo borrowingJdbcRepo;
    private final AuthorsJdbcRepo authorsJdbcRepo;

    @Override
    public BookDto getMostBorrowedBookJpa() {
        Book book = borrowingRepo.findMostBorrowedBook();
        log.info("Most borrowed book: {}", book.getTitle());
        return bookMapper.toBookDto(book);
    }

    @Override
    public BookDto getMostBorrowedBookJooq() {
        Book book = borrowingJooqRepo.findMostBorrowedBook();
        log.info("Most borrowed book: {}", book.getTitle());
        return bookMapper.toBookDto(book);
    }

    @Override
    public BookDto getMostBorrowedBookJdbc() {
        Book book = borrowingJdbcRepo.findMostBorrowedBook();
        log.info("Most borrowed book: {}", book.getTitle());
        return bookMapper.toBookDto(book);
    }

    @Override
    public Map<Integer, Integer> getAuthorsCountByBirthYearJpa() {
        List<Object[]> results = authorRepo.countAuthorsByBirthYear();

        return results.stream()
                .collect(Collectors.toMap(
                        result -> ((Number) result[0]).intValue(),
                        result -> ((Number) result[1]).intValue()
                ));
    }

    @Override
    public Map<Integer, Integer> getAuthorsCountByBirthYearJooq() {
        return authorJooqRepo.countAuthorsByBirthYear();
    }

    @Override
    public Map<Integer, Integer> getAuthorsCountByBirthYearJdbc() {
        return  authorsJdbcRepo.countAuthorsByBirthYear();
    }

    @Override
    public List<BookDto> getBooksByUserIdJpa(Long readerId) {
        List<Book> books = borrowingRepo.findBooksByReaderId(readerId);

        return books.stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public List<BookDto> getBooksByUserIdJooq(Long readerId) {
        List<Book> books = borrowingJooqRepo.findBooksByReaderId(readerId);

        return books.stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public List<BookDto> getBooksByUserIdJdbc(Long readerId) {
        List<Book> books = borrowingJdbcRepo.findBooksByReaderId(readerId);

        return books.stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
