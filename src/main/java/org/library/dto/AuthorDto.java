package org.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Автор")
public class AuthorDto {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String country;
}