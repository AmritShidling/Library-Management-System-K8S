package com.ericsson.library.borrowing_service;

import com.ericsson.library.borrowing_service.enums.BorrowStatus;
import com.ericsson.library.borrowing_service.exception.BookNotAvailableException;
import com.ericsson.library.borrowing_service.exception.BorrowRecordNotFoundException;
import com.ericsson.library.borrowing_service.exception.InvalidBorrowStatusException;
import com.ericsson.library.borrowing_service.exception.UserNotFoundException;
import com.ericsson.library.borrowing_service.model.BorrowRecord;
import com.ericsson.library.borrowing_service.repository.BorrowRecordRepository;
import com.ericsson.library.borrowing_service.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalDateTime.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BorrowingServiceApplicationTests {

	@InjectMocks
	private BorrowService borrowService;

	@Mock
	private BorrowRecordRepository borrowRepository;

	@Mock
	private RestTemplate restTemplate;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

	}



//	@Test
//	void borrowBook_UserNotFound_ReturnsNull() {
//		// Arrange
//		Long userId = 1L;
//		Long bookId = 2L;
//
//		when(restTemplate.getForObject("http://USER-SERVICE/v1/users/" + userId, Object.class))
//				.thenReturn(null);
//
//		// Act & Assert
//		UserNotFoundException exception = assertThrows(
//				UserNotFoundException.class,
//				() -> borrowService.borrowBook(userId, bookId)
//		);
//		assertEquals("User with ID " + userId + " not found", exception.getMessage());
//		verify(borrowRepository, never()).save(any());
//	}

//	@Test
//	void borrowBook_UserNotFound_ThrowsException() {
//		// Arrange
//		Long userId = 1L;
//		Long bookId = 2L;
//
//		when(restTemplate.getForObject("http://USER-SERVICE/v1/users/" + userId, Object.class))
//				.thenThrow(HttpClientErrorException.NotFound.class);
//
//		// Act & Assert
//		assertThrows(UserNotFoundException.class, () -> borrowService.borrowBook(userId, bookId));
//		verify(borrowRepository, never()).save(any());
//	}
//




	@Test
	void returnBook_RecordNotFound() {
		// Arrange
		Long borrowId = 1L;
		when(borrowRepository.findById(borrowId)).thenReturn(Optional.empty());

		// Act & Assert
		BorrowRecordNotFoundException exception = assertThrows(
				BorrowRecordNotFoundException.class,
				() -> borrowService.returnBook(borrowId)
		);
		assertEquals("Borrow record with ID " + borrowId + " not found", exception.getMessage());
	}


}