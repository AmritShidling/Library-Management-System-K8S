package com.ericsson.library;

import com.ericsson.library.controller.BookController;
import com.ericsson.library.dto.BookRequestDTO;
import com.ericsson.library.dto.BookResponseDTO;
import com.ericsson.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookRequestDTO sampleRequestDTO;
    private BookResponseDTO sampleResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample DTOs
        sampleRequestDTO = new BookRequestDTO();
        sampleRequestDTO.setTitle("Sample Book");
        sampleRequestDTO.setAuthor("Author Name");
        sampleRequestDTO.setIsbn("123456789");
        sampleRequestDTO.setAvailable(true);

        sampleResponseDTO = new BookResponseDTO();
        sampleResponseDTO.setId(1L);
        sampleResponseDTO.setTitle("Sample Book");
        sampleResponseDTO.setAuthor("Author Name");
        sampleResponseDTO.setIsbn("123456789");
        sampleResponseDTO.setAvailable(true);
    }

    @Test
    void createBook_ShouldReturnCreatedBook() {
        when(bookService.createBook(any(BookRequestDTO.class))).thenReturn(sampleResponseDTO);

        ResponseEntity<BookResponseDTO> response = bookController.createBook(sampleRequestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleResponseDTO, response.getBody());
        verify(bookService, times(1)).createBook(sampleRequestDTO);
    }

    @Test
    void getBook_ShouldReturnBook_WhenFound() {
        when(bookService.getBookById(1L)).thenReturn(sampleResponseDTO);

        ResponseEntity<BookResponseDTO> response = bookController.getBook(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleResponseDTO, response.getBody());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getBook_ShouldThrowException_WhenNotFound() {
        when(bookService.getBookById(1L)).thenThrow(new RuntimeException("Book not found"));

        assertThrows(RuntimeException.class, () -> bookController.getBook(1L));
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        List<BookResponseDTO> books = Arrays.asList(sampleResponseDTO, new BookResponseDTO());
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BookResponseDTO>> response = bookController.getAllBooks();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(books, response.getBody());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        when(bookService.updateBook(eq(1L), any(BookRequestDTO.class))).thenReturn(sampleResponseDTO);

        ResponseEntity<BookResponseDTO> response = bookController.updateBook(1L, sampleRequestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleResponseDTO, response.getBody());
        verify(bookService, times(1)).updateBook(1L, sampleRequestDTO);
    }

    @Test
    void deleteBook_ShouldReturnOk() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void updateBookAvailability_ShouldReturnUpdatedBook() {
        when(bookService.updateAvailability(1L, false)).thenReturn(sampleResponseDTO);

        ResponseEntity<BookResponseDTO> response = bookController.updateBookAvailability(1L, false);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleResponseDTO, response.getBody());
        verify(bookService, times(1)).updateAvailability(1L, false);
    }

    @Test
    void checkBookAvailability_ShouldReturnAvailabilityStatus() {
        when(bookService.isBookAvailable(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = bookController.checkBookAvailability(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(bookService, times(1)).isBookAvailable(1L);
    }
}
