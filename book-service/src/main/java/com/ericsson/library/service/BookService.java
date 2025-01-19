package com.ericsson.library.service;

import com.ericsson.library.dto.BookRequestDTO;
import com.ericsson.library.dto.BookResponseDTO;
import com.ericsson.library.entity.Book;
import com.ericsson.library.exception.BookNotFoundException;
import com.ericsson.library.exception.BookValidationException;
import com.ericsson.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("bookServiceImpl")
public class BookService {

    private final BookRepository bookRepository;

    // Create a new book
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        log.info("Attempting to create new book: {}", bookRequestDTO.getTitle());
        validateBook(bookRequestDTO);

        // Convert DTO to entity and save it
        Book book = mapToEntity(bookRequestDTO);
        Book savedBook = bookRepository.save(book);

        log.info("Successfully created book with ID: {}", savedBook.getId());

        // Convert the saved entity back to DTO
        return mapToResponseDTO(savedBook);
    }

    // Update an existing book
    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        log.info("Attempting to update book with ID: {}", id);
        validateBook(bookRequestDTO);

        // Fetch existing book
        Book existingBook = getBookByIdEntity(id);

        // Update fields
        existingBook.setTitle(bookRequestDTO.getTitle());
        existingBook.setAuthor(bookRequestDTO.getAuthor());
        existingBook.setIsbn(bookRequestDTO.getIsbn());
        existingBook.setAvailable(bookRequestDTO.getAvailable());

        // Save updated book and convert to DTO
        Book updatedBook = bookRepository.save(existingBook);
        log.info("Successfully updated book with ID: {}", updatedBook.getId());
        return mapToResponseDTO(updatedBook);
    }

    // Get a single book by ID
    public BookResponseDTO getBookById(Long id) {
        log.debug("Fetching book with ID: {}", id);
        Book book = getBookByIdEntity(id);
        return mapToResponseDTO(book);
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        log.info("Attempting to delete book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            log.error("Book not found with ID: {}", id);
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Successfully deleted book with ID: {}", id);
    }

    // Get all books
    public List<BookResponseDTO> getAllBooks() {
        log.info("Attempting to get all books");
        return bookRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Update book availability
    public BookResponseDTO updateAvailability(Long bookId, boolean available) {
        log.info("Attempting to update availability of book with ID: {}", bookId);
        Book book = getBookByIdEntity(bookId);
        book.setAvailable(available);

        Book updatedBook = bookRepository.save(book);
        log.info("Successfully updated availability of book with ID: {}", updatedBook.getId());
        return mapToResponseDTO(updatedBook);
    }

    // Check if a book is available
    public boolean isBookAvailable(Long bookId) {
        log.info("Checking availability of book with ID: {}", bookId);
        return bookRepository.findById(bookId)
                .map(Book::isAvailable)
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found", bookId);
                    return new BookNotFoundException(bookId);
                });
    }

    // Search books by query
    public List<BookResponseDTO> searchBooks(String query) {
        log.info("Searching books with query: {}", query);
        return bookRepository.searchBooks(query).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Private helper methods
    private void validateBook(BookRequestDTO bookRequestDTO) {
        if (bookRequestDTO.getTitle() == null || bookRequestDTO.getTitle().trim().isEmpty()) {
            log.error("Book validation failed: title is required");
            throw new BookValidationException("Book title is required");
        }
        if (bookRequestDTO.getAuthor() == null || bookRequestDTO.getAuthor().trim().isEmpty()) {
            log.error("Book validation failed: author is required");
            throw new BookValidationException("Book author is required");
        }
    }

    private Book getBookByIdEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with ID: {}", id);
                    return new BookNotFoundException(id);
                });
    }

    private Book mapToEntity(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setAvailable(dto.getAvailable());
        return book;
    }

    private BookResponseDTO mapToResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setAvailable(book.isAvailable());
        return dto;
    }
}
