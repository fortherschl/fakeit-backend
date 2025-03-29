package com.fakeit.fakeit.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticationDto {
    private String email;
    private String password;
}
