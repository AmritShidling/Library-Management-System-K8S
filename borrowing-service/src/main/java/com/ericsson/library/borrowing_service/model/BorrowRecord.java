package com.ericsson.library.borrowing_service.model;

import com.ericsson.library.borrowing_service.dto.Book;
import com.ericsson.library.borrowing_service.dto.User;
import com.ericsson.library.borrowing_service.enums.BorrowStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private LocalDateTime borrowDate;

    private LocalDateTime returnDate;
    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    @Transient // Not persisted in the database
    private User user;

    @Transient // Not persisted in the database
    private Book book;

}