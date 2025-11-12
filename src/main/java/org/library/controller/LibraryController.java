package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.converter.DtoBookConverter;
import org.library.generated.api.DefaultApi;
import org.library.generated.model.BookDto;
import org.library.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LibraryController implements DefaultApi {
    private final LibraryService libraryService;
    private final DtoBookConverter dtoBookConverter;

    @Override
    public ResponseEntity<BookDto> getMostBorrowedBook() {
        var bookDto = libraryService.getMostBorrowedBookJpa();
        BookDto res = dtoBookConverter.toBookDto(bookDto);
        return ResponseEntity.ok().body(res);
    }

    @Override
    public ResponseEntity<Map<String, Integer>> getAuthorsCountBirthYear() {
        Map<Integer, Integer> res = libraryService.getAuthorsCountByBirthYearJpa();

        Map<String, Integer> converted = res.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        Map.Entry::getValue
                ));

        return ResponseEntity.ok().body(converted);
    }

    @Override
    public ResponseEntity<List<BookDto>> getBooksByReaderId(@PathVariable Long readerId) {
        List<org.library.dto.BookDto> bookDtos = libraryService.getBooksByUserIdJpa(readerId);
        var res = bookDtos.stream()
                .map(dtoBookConverter::toBookDto)
                .toList();
        return ResponseEntity.ok().body(res);
    }
}
