package com.fakeit.fakeit.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserCreateDto {
    private String usuarioId;
    private String nombreUsuario;
    private String correo;
    private String password;
    private String fotoPerfil;
    private String fechaUnion;
    private String ultimaSesion;
    private int puntosTotales;
    private String rango;
    private List<String> grupos;
}
