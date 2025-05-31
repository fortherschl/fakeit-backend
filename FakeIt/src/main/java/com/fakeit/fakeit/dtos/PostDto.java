package com.fakeit.fakeit.dtos;

import lombok.Data;

import com.google.cloud.Timestamp;

@Data
public class PostDto {
    private String publicacionId;
    private String titulo;
    private String urlImagen;
    private boolean real;
    private String usuarioId;
    private String grupoId;
    private Timestamp fechaPublicacion;
}
