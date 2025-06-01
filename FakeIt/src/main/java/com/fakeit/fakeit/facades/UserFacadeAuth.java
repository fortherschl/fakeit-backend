package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.UserCreateDto;
import com.fakeit.fakeit.services.AuthenticationService;
import com.fakeit.fakeit.services.NewUserService;
import com.fakeit.fakeit.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserFacadeAuth {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserFacadeAuth(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    public String login(String email, String password) {
        return authenticationService.authWithFirebase(email, password);
    }

    public String register(UserCreateDto dto) {
        String token = authenticationService.register(dto.getCorreo(), dto.getPassword(), dto.getNombreUsuario());

        userService.createUser(dto);
        return token;
    }
}
