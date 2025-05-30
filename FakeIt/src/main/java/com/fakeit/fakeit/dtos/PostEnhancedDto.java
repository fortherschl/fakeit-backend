package com.fakeit.fakeit.dtos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class PostEnhancedDto {
    private String publicacionId;
    private String titulo;
    private String urlImagen;
    private Boolean real;
    private String usuarioId;
    private String nombreUsuario;
    private String fotoPerfil;
    private String grupoId;
    private Timestamp fechaPublicacion;
}
