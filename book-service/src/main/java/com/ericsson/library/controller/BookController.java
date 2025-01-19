package com.ericsson.library.controller;
//
//import com.ericsson.library.entity.Book;
//import com.ericsson.library.repository.BookRepository;
//import com.ericsson.library.service.BookService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//@RestController
//@RequestMapping("/v1/books")
//@RequiredArgsConstructor
//public class BookController {
//
//    @Qualifier("bookServiceImpl")
//    private final BookService bookService;
//
//    @PostMapping
//    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
//        return ResponseEntity.ok(bookService.createBook(book));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Book> getBook(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(bookService.getBookById(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Book>> getAllBooks() {
//        return ResponseEntity.ok(bookService.getAllBooks());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @Valid @RequestBody Book book) {
//        return ResponseEntity.ok(bookService.updateBook(id, book));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
//        bookService.deleteBook(id);
//        return ResponseEntity.ok().build();
//    }
//
//
//    @PutMapping("/{bookId}/availability")
//    public ResponseEntity<Book> updateBookAvailability(
//            @PathVariable("bookId") Long bookId,
//            @RequestParam("available") boolean available) {
//        return ResponseEntity.ok(bookService.updateAvailability(bookId, available));
//    }
//
//    @GetMapping("/{bookId}/availability")
//    public ResponseEntity<Boolean> checkBookAvailability(@PathVariable("bookId") Long bookId) {
//        return ResponseEntity.ok(bookService.isBookAvailable(bookId));
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Book>> searchBooks(@RequestParam("query") String query) {
//        return ResponseEntity.ok(bookService.searchBooks(query));
//    }
//}


import com.ericsson.library.dto.BookRequestDTO;
import com.ericsson.library.dto.BookResponseDTO;
import com.ericsson.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        return ResponseEntity.ok(bookService.createBook(bookRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBook(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{bookId}/availability")
    public ResponseEntity<BookResponseDTO> updateBookAvailability(
            @PathVariable("bookId") Long bookId,
            @RequestParam("available") boolean available) {
        return ResponseEntity.ok(bookService.updateAvailability(bookId, available));
    }

    @GetMapping("/{bookId}/availability")
    public ResponseEntity<Boolean> checkBookAvailability(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.ok(bookService.isBookAvailable(bookId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(@RequestParam("query") String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }
}
