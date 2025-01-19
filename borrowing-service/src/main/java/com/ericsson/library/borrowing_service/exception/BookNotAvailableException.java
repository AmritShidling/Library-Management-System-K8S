package com.ericsson.library.borrowing_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(Long bookId) {
        super("Book with ID " + bookId + " is not available for borrowing");
    }

}