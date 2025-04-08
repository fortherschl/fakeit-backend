package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.UserAuthenticationDto;
import com.fakeit.fakeit.dtos.UserRegisterDto;
import com.fakeit.fakeit.facades.UserFacade;
import com.fakeit.fakeit.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthenticationController {


    private final UserFacade userFacade;

    public AuthenticationController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/home")
    public String home() {
        return "Hello World";
    }

    @PostMapping("/login")
    public ResponseEntity<String> getLogin(@RequestBody UserAuthenticationDto userAuthenticationDto) {
        return ResponseEntity.ok(userFacade.login(userAuthenticationDto.getEmail(), userAuthenticationDto.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto userRegisterDto) {
        return ResponseEntity.ok(userFacade.register(userRegisterDto.getEmail(), userRegisterDto.getPassword(), userRegisterDto.getUsername()));
    }
}
