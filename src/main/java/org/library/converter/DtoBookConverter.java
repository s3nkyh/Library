package org.library.converter;

import org.library.dto.BookDto;
import org.springframework.stereotype.Component;

@Component
public class DtoBookConverter {
    public org.library.generated.model.BookDto toBookDto(BookDto  bookDto) {
        var author = new org.library.generated.model.AuthorDto()
                .id(bookDto.getAuthor().getId())
                .name(bookDto.getAuthor().getName())
                .birthDate(bookDto.getAuthor().getBirthDate())
                .country(bookDto.getAuthor().getCountry());

        var genre = new org.library.generated.model.GenreDto()
                .id(bookDto.getGenre().getId())
                .name(bookDto.getGenre().getName());

        var book = new org.library.generated.model.BookDto()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .author(author)
                .genre(genre)
                .publishedYear(bookDto.getPublishedYear());

        return book;
    }
}
