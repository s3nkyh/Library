package org.library.repo.jpa;

import org.library.models.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReaderRepo extends JpaRepository<Reader, Long> {
}
