package com.ericsson.library.borrowing_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BorrowRecordNotFoundException extends RuntimeException {
    public BorrowRecordNotFoundException(Long borrowId) {
        super("Borrow record with ID " + borrowId + " not found");
    }
}