package org.library.repo.jpa;

import org.library.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {
    @Query("""
                SELECT YEAR(a.birthDate) AS year, COUNT(a) 
                FROM Author a 
                GROUP BY YEAR(a.birthDate)
            """)
    List<Object[]> countAuthorsByBirthYear();
}
