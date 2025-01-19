package com.ericsson.library.user_service.utils;

import com.ericsson.library.user_service.dto.UserRequestDTO;
import com.ericsson.library.user_service.dto.UserResponseDTO;
import com.ericsson.library.user_service.model.User;

public class UserMapper {

    public static User mapToEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        return user;
    }

    public static UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        return userResponseDTO;
    }
}