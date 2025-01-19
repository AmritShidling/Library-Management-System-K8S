package com.ericsson.library.borrowing_service.dto;


import lombok.Data;

@Data
public class Book {
    private Long id;
    private String title;
    private String author;

}