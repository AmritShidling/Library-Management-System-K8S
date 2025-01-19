package com.ericsson.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(Long bookId) {
        super("Book with ID " + bookId + " is not available");
    }
}