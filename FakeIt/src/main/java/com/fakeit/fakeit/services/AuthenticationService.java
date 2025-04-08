package com.fakeit.fakeit.services;

public interface AuthenticationService {

    String authWithFirebase(String email, String password);

    String register(String email, String password, String username);
}
