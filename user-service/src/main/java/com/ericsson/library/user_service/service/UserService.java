package com.ericsson.library.user_service.service;
import com.ericsson.library.user_service.dto.UserRequestDTO;
import com.ericsson.library.user_service.dto.UserResponseDTO;
import com.ericsson.library.user_service.exception.UserNotFoundException;
import com.ericsson.library.user_service.exception.UserValidationException;
import com.ericsson.library.user_service.model.User;
import com.ericsson.library.user_service.repository.UserRepository;
import com.ericsson.library.user_service.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        log.info("Attempting to create new user: {}", userRequestDTO.getName());

        validateUser(userRequestDTO);
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new UserValidationException("User with this email already exists");
        }
        if (!isValidEmail(userRequestDTO.getEmail())) {
            throw new UserValidationException("Enter a valid email");
        }

        User user = UserMapper.mapToEntity(userRequestDTO);
        User savedUser = userRepository.save(user);

        log.info("User created successfully with ID: {}", savedUser.getId());
        return UserMapper.mapToResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserNotFoundException(id);
                });

        return UserMapper.mapToResponseDTO(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        log.info("Attempting to update user with ID: {}", id);

        validateUser(userRequestDTO);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserNotFoundException(id);
                });

        // Update fields
        existingUser.setName(userRequestDTO.getName());
        existingUser.setEmail(userRequestDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);
        log.info("Successfully updated user with ID: {}", updatedUser.getId());

        return UserMapper.mapToResponseDTO(updatedUser);
    }

    public List<UserResponseDTO> getUsers() {
        log.info("Attempting to get all users");

        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void validateUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getName() == null || userRequestDTO.getName().trim().isEmpty()) {
            log.error("User validation failed: name is required");
            throw new UserValidationException("User name is required");
        }
        if (userRequestDTO.getEmail() == null || userRequestDTO.getEmail().trim().isEmpty()) {
            log.error("User validation failed: email is required");
            throw new UserValidationException("User email is required");
        }
        if (!isValidEmail(userRequestDTO.getEmail())) {
            log.error("User validation failed: invalid email format");
            throw new UserValidationException("Invalid email format");
        }
    }
}
