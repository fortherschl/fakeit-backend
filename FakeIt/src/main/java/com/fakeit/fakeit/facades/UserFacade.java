package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.services.AuthenticationService;
import com.fakeit.fakeit.services.NewUserService;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final AuthenticationService authenticationService;
    private final NewUserService newUserService;

    public UserFacade(AuthenticationService authenticationService, NewUserService newUserService) {
        this.authenticationService = authenticationService;
        this.newUserService = newUserService;
    }

    public String login(String email, String password) {
        return authenticationService.authWithFirebase(email, password);
    }

    public String register(String email, String password, String username) {
        String token = authenticationService.authWithFirebase(email, password);

        newUserService.addNewUser(email, username);
        return token;
    }
}
