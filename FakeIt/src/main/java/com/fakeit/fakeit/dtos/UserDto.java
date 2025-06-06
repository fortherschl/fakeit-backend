package com.fakeit.fakeit.dtos;

import com.google.cloud.Timestamp;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String usuarioId;
    private String nombreUsuario;
    private String correo;
    private String fotoPerfil;
    private Timestamp fechaUnion;
    private Timestamp ultimaSesion;
    private int puntosTotales;
    private String rango;
    private boolean administrador;
    private List<String> grupos;

}
