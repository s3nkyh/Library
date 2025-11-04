package org.library.service;

import org.library.dto.BookDto;

import java.util.List;
import java.util.Map;

public interface LibraryService {
    BookDto getMostBorrowedBookJpa();

    BookDto getMostBorrowedBookJooq();

    BookDto getMostBorrowedBookJdbc();

    Map<Integer, Integer> getAuthorsCountByBirthYearJpa();

    Map<Integer, Integer> getAuthorsCountByBirthYearJooq();

    Map<Integer, Integer> getAuthorsCountByBirthYearJdbc();

    List<BookDto> getBooksByUserIdJpa(Long readerId);

    List<BookDto> getBooksByUserIdJooq(Long readerId);

    List<BookDto> getBooksByUserIdJdbc(Long readerId);
}