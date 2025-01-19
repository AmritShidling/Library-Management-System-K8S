package com.ericsson.library;

import com.ericsson.library.dto.BookRequestDTO;
import com.ericsson.library.dto.BookResponseDTO;
import com.ericsson.library.entity.Book;
import com.ericsson.library.exception.BookNotFoundException;
import com.ericsson.library.exception.BookValidationException;
import com.ericsson.library.repository.BookRepository;
import com.ericsson.library.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    @Qualifier("bookServiceImpl")
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    private Book book;
    private BookRequestDTO requestDTO;
    private BookResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize book entity
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setAvailable(true);
        book.setIsbn("123456789");

        // Initialize DTOs
        requestDTO = new BookRequestDTO();
        requestDTO.setTitle("Test Book");
        requestDTO.setAuthor("Test Author");
        requestDTO.setAvailable(true);
        requestDTO.setIsbn("123456789");

        responseDTO = new BookResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Test Book");
        responseDTO.setAuthor("Test Author");
        responseDTO.setAvailable(true);
        responseDTO.setIsbn("123456789");
    }

    @Test
    void createBook_success() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO createdBook = bookService.createBook(requestDTO);

        assertNotNull(createdBook);
        assertEquals("Test Book", createdBook.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_validationFailure() {
        requestDTO.setTitle("");
        assertThrows(BookValidationException.class, () -> bookService.createBook(requestDTO));
    }

    @Test
    void updateBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO updatedBook = bookService.updateBook(1L, requestDTO);

        assertNotNull(updatedBook);
        assertEquals("Test Book", updatedBook.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(1L, requestDTO));
    }

    @Test
    void getBookById_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponseDTO fetchedBook = bookService.getBookById(1L);

        assertNotNull(fetchedBook);
        assertEquals("Test Book", fetchedBook.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void deleteBook_success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_notFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(1L));
    }

    @Test
    void getAllBooks_success() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponseDTO> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void updateAvailability_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO updatedBook = bookService.updateAvailability(1L, false);

        assertNotNull(updatedBook);
        assertFalse(updatedBook.isAvailable());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateAvailability_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.updateAvailability(1L, false));
    }

    @Test
    void isBookAvailable_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        boolean available = bookService.isBookAvailable(1L);

        assertTrue(available);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void isBookAvailable_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.isBookAvailable(1L));
    }
}
