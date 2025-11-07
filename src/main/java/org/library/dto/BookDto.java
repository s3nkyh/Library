package org.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Книга")
public class BookDto {
    private Long id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;
    private Integer publishedYear;
}
