package org.library.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.BaseIntegrationTest;
import org.library.models.Author;
import org.library.repo.jpa.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorJpaRepoTest extends BaseIntegrationTest {

    @Autowired
    private AuthorRepo authorRepo;


    @BeforeEach
    void setUp() {
        authorRepo.deleteAll();

        authorRepo.save(Author.builder()
                .name("Author 1")
                .birthDate(LocalDate.of(1980, 5, 10))
                .country("RU")
                .build());

        authorRepo.save(Author.builder()
                .name("Author 2")
                .birthDate(LocalDate.of(1980, 8, 20))
                .country("US")
                .build());

        authorRepo.save(Author.builder()
                .name("Author 3")
                .birthDate(LocalDate.of(1990, 2, 15))
                .country("DE")
                .build());
    }

    @Test
    void testCountAuthorsByBirthYear() {
        Map<Integer, Integer> map = authorRepo.countAuthorsByBirthYear().stream()
                .collect(Collectors.toMap(
                        result -> ((Number) result[0]).intValue(),
                        result -> ((Number) result[1]).intValue()
                ));

        assertThat(map)
                .containsEntry(1980, 2)
                .containsEntry(1990, 1)
                .hasSize(2);
    }
}