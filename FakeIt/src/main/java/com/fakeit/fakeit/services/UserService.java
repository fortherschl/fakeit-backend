package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.UserCreateDto;
import com.fakeit.fakeit.dtos.UserDto;
import com.fakeit.fakeit.dtos.UserUpdateDto;
import com.fakeit.fakeit.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String createUser(UserCreateDto userCreateDto);
    Optional<UserDto> getUserById(String id);
    List<UserDto> searchUsersByUsername(String query);
    Optional<UserDto> getUserByEmail(String email);
    UserDto updateUser(String id, UserUpdateDto updateDto);
    List<UserDto> getAllUsers();
}
