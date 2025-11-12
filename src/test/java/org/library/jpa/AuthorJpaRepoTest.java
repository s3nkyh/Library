package org.library.jpa;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.BaseIntegrationTest;
import org.library.model.Author;
import org.library.repo.jpa.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
public class AuthorJpaRepoTest {
    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18.0")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("password");

    @DynamicPropertySource
    static void registerPgContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

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