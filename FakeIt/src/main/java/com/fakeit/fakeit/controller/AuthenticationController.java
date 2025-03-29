package com.fakeit.fakeit.controller;

import com.fakeit.fakeit.dtos.UserAuthenticationDto;
import com.fakeit.fakeit.services.AuthenticationService;
import com.fakeit.fakeit.services.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/home")
    public String home() {
        return "Hello World";
    }

    @PostMapping("/login")
    public ResponseEntity<String> getLogin (@RequestBody UserAuthenticationDto userAuthenticationDto) {
        return ResponseEntity.ok( authenticationService.authWithFirebase(userAuthenticationDto.getEmail(), userAuthenticationDto.getPassword()) );
    }
    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody UserAuthenticationDto userAuthenticationDto) {
        return ResponseEntity.ok( authenticationService.register(userAuthenticationDto.getEmail(), userAuthenticationDto.getPassword()) );
    }


}
