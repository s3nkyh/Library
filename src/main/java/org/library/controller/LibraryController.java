package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.BookDto;
import org.library.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping("/books/most-borrowed")
    public ResponseEntity<BookDto> getMostBorrowed() {
        BookDto res = libraryService.getMostBorrowedBookJpa();
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/authors/birth-years/stats")
    public ResponseEntity<Map<Integer, Integer>> getAuthorsCountByBirthYear() {
        Map<Integer, Integer> res = libraryService.getAuthorsCountByBirthYearJpa();
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/users/{readerId}/borrowed-books")
    public ResponseEntity<List<BookDto>> getBooksByUserId(@PathVariable Long readerId) {
        List<BookDto> res = libraryService.getBooksByUserIdJpa(readerId);
        return ResponseEntity.ok().body(res);
    }
}
