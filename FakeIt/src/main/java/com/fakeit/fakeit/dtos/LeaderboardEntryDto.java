package com.fakeit.fakeit.dtos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class LeaderboardEntryDto {
    private String publicacionId;
    private String grupoId;
    private String usuarioId;
    private String nombreUsuario;
    private String fotoPerfil;
    private String titulo;
    private Boolean real;
    private String urlImagen;
    private int enganadosCount;     // participantes “engañados”
    private int tamanioGrupo;
    private int puntosRecibidos;
    private Timestamp processedAt;
}
