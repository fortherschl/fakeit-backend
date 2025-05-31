package com.fakeit.fakeit.dtos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class PostHiddenDto {
    private String publicacionId;
    private String titulo;
    private String urlImagen;
    private String usuarioId;
    private String grupoId;
    private Timestamp fechaPublicacion;
}
