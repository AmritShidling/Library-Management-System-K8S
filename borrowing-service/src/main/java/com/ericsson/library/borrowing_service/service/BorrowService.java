package com.ericsson.library.borrowing_service.service;

import com.ericsson.library.borrowing_service.dto.Book;
import com.ericsson.library.borrowing_service.dto.BorrowRecordDTO;
import com.ericsson.library.borrowing_service.dto.User;
import com.ericsson.library.borrowing_service.enums.BorrowStatus;
import com.ericsson.library.borrowing_service.exception.BookNotAvailableException;
import com.ericsson.library.borrowing_service.exception.BorrowRecordNotFoundException;
import com.ericsson.library.borrowing_service.exception.InvalidBorrowStatusException;
import com.ericsson.library.borrowing_service.exception.UserNotFoundException;
import com.ericsson.library.borrowing_service.model.BorrowRecord;
import com.ericsson.library.borrowing_service.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowService {
    private final BorrowRecordRepository borrowRepository;
    private final RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    private String getBookServiceUrl() {
        return environment.getProperty("app.service.book-service-url");
    }

    private String getUserServiceUrl() {
        return environment.getProperty("app.service.user-service-url");
    }
    public BorrowRecord borrowBook(Long userId, Long bookId) {
        log.info("Attempting to borrow book with ID {} for user with ID {}", bookId, userId);

        String userServiceUrl = getUserServiceUrl() + "/v1/users/" + userId;


        try {
//            Object userResponse = restTemplate.getForObject("http://USER-SERVICE/v1/users/" + userId, Object.class);
            User userResponse = restTemplate.getForObject(userServiceUrl, User.class);
            if (userResponse == null) {
                log.warn("User with ID {} not found", userId);
                throw new UserNotFoundException(userId);
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.error("User with ID {} not found", userId);
            throw new UserNotFoundException(userId);
        } catch (Exception e) {
            log.error("Unexpected error while checking user: {}", e.getMessage());
            throw e; // Rethrow the original exception instead of wrapping it
        }




        try{
            // Check if book is available
            String bookServiceUrl = getBookServiceUrl() + "/v1/books/"+bookId+"/availability";
            Boolean isAvailable  = restTemplate.getForObject(bookServiceUrl, Boolean.class);

//            Boolean isAvailable = restTemplate.getForObject(
//                    "http://BOOK-SERVICE/v1/books/{bookId}/availability",
//                    Boolean.class,
//                    bookId
//            );

            if (isAvailable == null || !isAvailable) {
                log.warn("Book with ID {} is not available", bookId);
                throw new BookNotAvailableException(bookId);
            }
            // Update book availability
            restTemplate.put(
                    bookServiceUrl+"?available=false",
                    null,
                    bookId
            );
        }
        catch (BookNotAvailableException e) {
            log.error("Book with ID {} not found", userId);
            throw new BookNotAvailableException(userId);
        }
        catch (HttpClientErrorException.NotFound e) {
            log.error("Book with ID {} not found", userId);
            throw new BookNotAvailableException(userId);
        } catch (Exception e) {
            log.error("Error while checking user: {}", e.getMessage());
            throw new RuntimeException("Error communicating with BOOK-SERVICE");
        }



        // Create borrow record
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUserId(userId);
        borrowRecord.setBookId(bookId);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowStatus.BORROWED);

        return borrowRepository.save(borrowRecord);
    }

    public List<BorrowRecordDTO> searchBorrowingHistory(String userName, String bookTitle) {
        // Fetch all borrowing records
        List<BorrowRecord> borrowRecords = borrowRepository.findAll();
        log.info("Searching history for user {} and book {}", userName, bookTitle);
        return borrowRecords.stream().filter(record -> {
            // Fetch user details from user service
//            String userServiceUrl = "http://USER-SERVICE/v1/users/" + record.getUserId();
            String userServiceUrl = getUserServiceUrl() + "/v1/users/";
            User user = restTemplate.getForObject(userServiceUrl, User.class);

            // Fetch book details from book service
//            String bookServiceUrl = "http://BOOK-SERVICE/v1/books/" + record.getBookId();
            String bookServiceUrl = getBookServiceUrl() + "/v1/books/"+record.getBookId();
            Book book = restTemplate.getForObject(bookServiceUrl, Book.class);

            // Match user name and book title
            boolean userMatches = (userName == null || userName.isBlank()) ||
                    (user != null && user.getName().toLowerCase().contains(userName.toLowerCase()));
            boolean bookMatches = (bookTitle == null || bookTitle.isBlank()) ||
                    (book != null && book.getTitle().toLowerCase().contains(bookTitle.toLowerCase()));

            // Store user and book in a wrapper to make them available in the map stage
            record.setUser(user);
            record.setBook(book);

            return userMatches && bookMatches;
        }).map(record -> {
            // Map to DTO using user and book fetched in the filter stage
            return new BorrowRecordDTO(
                    record.getId(),
                    record.getUser() != null ? record.getUser().getName() : "Unknown User",
                    record.getBook() != null ? record.getBook().getTitle() : "Unknown Book",
                    record.getBorrowDate(),
                    record.getReturnDate()
            );
        }).collect(Collectors.toList());
    }

    public BorrowRecord returnBook(Long borrowId) {

        log.info("Attempting to return book with Borrow ID{}", borrowId);
        BorrowRecord borrowRecord = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new BorrowRecordNotFoundException(borrowId));

        if (borrowRecord.getStatus() != BorrowStatus.BORROWED) {
            throw new InvalidBorrowStatusException("Book is not currently borrowed");
        }

        String bookServiceUrl = getBookServiceUrl() + "/v1/books/"+borrowRecord.getBookId()+"/availability?available=true";

        restTemplate.put(
//                "http://BOOK-SERVICE/v1/books/{bookId}/availability?available=true",
                bookServiceUrl,
                null,
                borrowRecord.getBookId()
        );

        borrowRecord.setReturnDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowStatus.RETURNED);

        return borrowRepository.save(borrowRecord);
    }

    public List<BorrowRecord> getBorrowingHistory(Long userId) {
        return borrowRepository.findByUserId(userId);
    }




    public List<BorrowRecordDTO> getAllBorrowingHistory() {
        List<BorrowRecord> borrowRecords = borrowRepository.findAll();

        return borrowRecords.stream().map(record -> {
            // Fetch user details
            String userServiceUrl = getUserServiceUrl() + "/v1/users/" + record.getUserId();
            User user = restTemplate.getForObject(userServiceUrl, User.class);

            // Fetch book details
            String bookServiceUrl = getUserServiceUrl()+"/v1/books/" + record.getBookId();
            Book book = restTemplate.getForObject(bookServiceUrl, Book.class);

            // Map to DTO
            return new BorrowRecordDTO(
                    record.getId(),
                    user != null ? user.getName() : "Unknown User",
                    book != null ? book.getTitle() : "Unknown Book",
                    record.getBorrowDate(),
                    record.getReturnDate()
            );
        }).collect(Collectors.toList());
    }
}