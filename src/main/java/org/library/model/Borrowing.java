package org.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.library.converter.BorrowingStatusConverter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "borrowings")
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @Builder.Default
    @Column(name = "borrowed_date", nullable = false)
    private LocalDate borrowedDate = LocalDate.now();

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_date")
    private LocalDate returnedDate;

    @Builder.Default
    @Convert(converter = BorrowingStatusConverter.class)
    @Column(nullable = false, columnDefinition = "borrowing_status")
    private BorrowingStatus status = BorrowingStatus.BORROWED;
}
