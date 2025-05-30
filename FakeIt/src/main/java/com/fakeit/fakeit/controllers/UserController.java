package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.UserCreateDto;
import com.fakeit.fakeit.dtos.UserDto;
import com.fakeit.fakeit.dtos.UserUpdateDto;
import com.fakeit.fakeit.facades.UserFacade;
import com.fakeit.fakeit.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(userFacade.createUser(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable String id) {
        return userFacade.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        @GetMapping("/search")
        public ResponseEntity<List<UserDto>> getByEmailOrSearch(
                @RequestParam(required = false) String email,
                @RequestParam(required = false) String search) {
    
            if (email != null) {
                return userFacade.getUserByEmail(email)
                        .map(user -> ResponseEntity.ok(List.of(user)))
                        .orElse(ResponseEntity.notFound().build());
            } else if (search != null) {
                return ResponseEntity.ok(userFacade.searchByUsername(search));
            } else {
                return ResponseEntity.badRequest().build();
            }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable String id, @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userFacade.updateUser(id, dto));
    }
}



