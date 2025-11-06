package org.library.repo.jpa;

import org.library.model.Book;
import org.library.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepo extends JpaRepository<Borrowing, Long> {
    @Query("""
                SELECT b.book
                FROM Borrowing b
                GROUP BY b.book
                ORDER BY COUNT(b) DESC
                LIMIT 1
            """)
    Book findMostBorrowedBook();

    @Query("""
                SELECT b.book
                FROM Borrowing b
                WHERE b.reader.id = :readerId
            """)
    List<Book> findBooksByReaderId(@Param("readerId") Long readerId);

    List<Borrowing> findByReaderId(@Param("readerId") Long readerId);
}
