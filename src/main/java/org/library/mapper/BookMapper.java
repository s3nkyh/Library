package org.library.mapper;

import org.library.dto.BookDto;
import org.library.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {GenreMapper.class, AuthorMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BookMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "genre", source = "genre")
    BookDto toBookDto(Book book);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "genre", source = "genre")
    Book toBook(BookDto bookDto);
}
