package org.library.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDto {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String country;
}
