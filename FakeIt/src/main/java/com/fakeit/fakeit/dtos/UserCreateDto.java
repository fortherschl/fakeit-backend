package com.fakeit.fakeit.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserCreateDto {
    private String nombreUsuario;
    private String correo;
    private String password;
    private String fotoPerfil;
}
