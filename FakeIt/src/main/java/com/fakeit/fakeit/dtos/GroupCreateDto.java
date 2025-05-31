package com.fakeit.fakeit.dtos;

import lombok.Data;

@Data
public class GroupCreateDto {
    private String nombreGrupo;
    private String descripcion;
    private boolean publico;          // true = público
    private int maximoUsuarios = 30;  // por defecto
    private String fotoPerfil;        // opcional
}
