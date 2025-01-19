package com.ericsson.library.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequestDTO {
    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Author is required")
    private String author;

    @NotEmpty(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Availability status is required")
    private Boolean available;
}
