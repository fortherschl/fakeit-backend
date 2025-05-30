package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.UserAuthenticationDto;
import com.fakeit.fakeit.dtos.UserRegisterDto;
import com.fakeit.fakeit.facades.UserFacadeAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthenticationController {


    private final UserFacadeAuth userFacadeAuth;

    public AuthenticationController(UserFacadeAuth userFacadeAuth) {
        this.userFacadeAuth = userFacadeAuth;
    }

    @GetMapping("/home")
    public String home() {
        return "Hello World";
    }

    @PostMapping("/login")
    public ResponseEntity<String> getLogin(@RequestBody UserAuthenticationDto userAuthenticationDto) {
        return ResponseEntity.ok(userFacadeAuth.login(userAuthenticationDto.getEmail(), userAuthenticationDto.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto userRegisterDto) {
        return ResponseEntity.ok(userFacadeAuth.register(userRegisterDto.getEmail(), userRegisterDto.getPassword(), userRegisterDto.getUsername()));
    }
}
