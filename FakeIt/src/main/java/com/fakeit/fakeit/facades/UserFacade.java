package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.UserCreateDto;
import com.fakeit.fakeit.dtos.UserDto;
import com.fakeit.fakeit.dtos.UserUpdateDto;
import com.fakeit.fakeit.models.User;
import com.fakeit.fakeit.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public UserDto createUser(UserCreateDto dto) {
        return userService.getUserById(userService.createUser(dto)).orElseThrow(() -> new RuntimeException("User creation failed"));
    }

    public Optional<UserDto> getUserById(String id) {
        return userService.getUserById(id);
    }

    public List<UserDto> searchByUsername(String query) {
        return userService.searchUsersByUsername(query);
    }

    public Optional<UserDto> getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public UserDto updateUser(String id, UserUpdateDto dto) {
        return userService.updateUser(id, dto);
    }

    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
