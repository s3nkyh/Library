package org.library.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private Long id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;
    private Integer publishedYear;
}
