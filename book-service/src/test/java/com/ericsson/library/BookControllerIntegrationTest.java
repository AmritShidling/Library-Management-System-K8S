package com.ericsson.library;

import com.ericsson.library.dto.BookRequestDTO;
import com.ericsson.library.dto.BookResponseDTO;
import com.ericsson.library.entity.Book;
import com.ericsson.library.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setTitle("Integration Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("123-4567891234");
        testBook.setAvailable(true);
        bookRepository.save(testBook);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void testGetAllBooks() {
        String url = "http://localhost:" + port + "/v1/books";

        ResponseEntity<BookResponseDTO[]> response = restTemplate.getForEntity(url, BookResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals(testBook.getTitle(), response.getBody()[0].getTitle());
    }

    @Test
    void testCreateBook() {
        String url = "http://localhost:" + port + "/v1/books";

        BookRequestDTO newBook = new BookRequestDTO();
        newBook.setTitle("New Integration Test Book");
        newBook.setAuthor("New Author");
        newBook.setIsbn("987-6543219876");
        newBook.setAvailable(true);
        ResponseEntity<BookResponseDTO> response = restTemplate.postForEntity(url, newBook, BookResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newBook.getTitle(), response.getBody().getTitle());
    }

    @Test
    void testGetBookById() {
        String url = "http://localhost:" + port + "/v1/books/" + testBook.getId();

        ResponseEntity<BookResponseDTO> response = restTemplate.getForEntity(url, BookResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testBook.getTitle(), response.getBody().getTitle());
    }

    @Test
    void testUpdateBookAvailability() {
        String url = "http://localhost:" + port + "/v1/books/" + testBook.getId() + "/availability?available=false";

        restTemplate.put(url, null);

        Book updatedBook = bookRepository.findById(testBook.getId()).orElse(null);

        assertNotNull(updatedBook);
        assertFalse(updatedBook.isAvailable());
    }
}
