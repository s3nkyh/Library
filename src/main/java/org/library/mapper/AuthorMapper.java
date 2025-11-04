package org.library.mapper;

import org.library.dto.AuthorDto;
import org.library.models.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toAuthorDto(Author author);

    Author toAuthor(AuthorDto authorDto);
}
