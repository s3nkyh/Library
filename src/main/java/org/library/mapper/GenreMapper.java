package org.library.mapper;

import org.library.dto.GenreDto;
import org.library.model.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    Genre toGenre(GenreDto genreDto);

    GenreDto toGenreDto(Genre genre);
}
