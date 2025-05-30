package com.fakeit.fakeit.models;

import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {
    private boolean administrador = false;
    private String correo;
    private Timestamp fechaUnion;
    private String fotoPerfil;
    private List<Group> grupos;
    private String nombreUsuario;
    private int puntosTotales;
    private UserRank rango;
    private Timestamp ultimaSesion;
    private String usuarioId;

    public User(boolean admin,
                String email,
                Timestamp joinDate,
                String profilePicture,
                List<Group> groupss,
                String nombreUsuario,
                int puntosTotales,
                UserRank rango,
                Timestamp ultimaSesion,
                String usuarioId) {
        this.administrador = admin;
        this.correo = email;
        this.fechaUnion = joinDate;
        this.fotoPerfil = profilePicture;
        this.grupos = groupss;
        this.nombreUsuario = nombreUsuario;
        this.puntosTotales = puntosTotales;
        this.rango = rango;
        this.ultimaSesion = ultimaSesion;
        this.usuarioId = usuarioId;
    }
    public User(){

    }
}
