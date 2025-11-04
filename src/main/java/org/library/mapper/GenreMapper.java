package org.library.mapper;

import org.library.dto.GenreDto;
import org.library.models.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    Genre toGenre(GenreDto genreDto);

    GenreDto toGenreDto(Genre genre);
}
