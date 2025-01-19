package com.ericsson.library.user_service.controller;

import com.ericsson.library.user_service.dto.UserRequestDTO;
import com.ericsson.library.user_service.dto.UserResponseDTO;
import com.ericsson.library.user_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Validated UserRequestDTO userRequestDTO) {
        try {
            return ResponseEntity.ok(userService.createUser(userRequestDTO));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") Long id, @RequestBody @Validated UserRequestDTO userRequestDTO) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        try {
            return ResponseEntity.ok(userService.getUsers());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
