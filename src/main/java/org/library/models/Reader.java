package org.library.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "readers")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Builder.Default
    @Column(name = "membership_date")
    private LocalDate membershipDate = LocalDate.now();

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
