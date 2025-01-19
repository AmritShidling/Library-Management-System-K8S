package com.ericsson.library.borrowing_service.dto;

import java.time.LocalDateTime;

// BorrowRecordDTO.java
public class BorrowRecordDTO {

    private Long id;
    private String userName;
    private String bookTitle;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

    // Constructor
    public BorrowRecordDTO(Long id, String userName, String bookTitle, LocalDateTime borrowDate, LocalDateTime returnDate) {
        this.id = id;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters
    public Long getId(){
        return id;
    }
    public String getUserName() {
        return userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }
}
