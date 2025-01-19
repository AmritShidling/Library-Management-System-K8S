package com.ericsson.library.user_service;

import com.ericsson.library.user_service.dto.UserRequestDTO;
import com.ericsson.library.user_service.dto.UserResponseDTO;
import com.ericsson.library.user_service.exception.UserNotFoundException;
import com.ericsson.library.user_service.exception.UserValidationException;
import com.ericsson.library.user_service.model.User;
import com.ericsson.library.user_service.repository.UserRepository;
import com.ericsson.library.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class UserServiceApplicationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void shouldCreateUserSuccessfully() {
		// Arrange
		UserRequestDTO userRequestDTO = new UserRequestDTO("John Doe", "john.doe@example.com");
		User userEntity = new User("John Doe", "john.doe@example.com");
		User savedUser = new User("John Doe", "john.doe@example.com");
		savedUser.setId(1L);

		when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		// Act
		UserResponseDTO createdUser = userService.createUser(userRequestDTO);

		// Assert
		assertNotNull(createdUser);
		assertEquals("John Doe", createdUser.getName());
		assertEquals("john.doe@example.com", createdUser.getEmail());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenEmailAlreadyExists() {
		// Arrange
		UserRequestDTO userRequestDTO = new UserRequestDTO("John Doe", "john.doe@example.com");

		when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);

		// Act & Assert
		UserValidationException exception = assertThrows(UserValidationException.class, () -> {
			userService.createUser(userRequestDTO);
		});

		assertEquals("User with this email already exists", exception.getMessage());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenUserNotFound() {
		// Arrange
		Long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// Act & Assert
		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
			userService.getUserById(userId);
		});

		assertEquals("User with ID 1 not found", exception.getMessage());
	}

	@Test
	void shouldUpdateUserSuccessfully() {
		// Arrange
		Long userId = 1L;
		UserRequestDTO userRequestDTO = new UserRequestDTO("John Updated", "john.updated@example.com");
		User existingUser = new User("John Doe", "john.doe@example.com");
		existingUser.setId(userId);

		User updatedUser = new User("John Updated", "john.updated@example.com");
		updatedUser.setId(userId);

		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userRepository.save(existingUser)).thenReturn(updatedUser);

		// Act
		UserResponseDTO result = userService.updateUser(userId, userRequestDTO);

		// Assert
		assertNotNull(result);
		assertEquals("John Updated", result.getName());
		assertEquals("john.updated@example.com", result.getEmail());
		verify(userRepository, times(1)).save(existingUser);
	}
}
