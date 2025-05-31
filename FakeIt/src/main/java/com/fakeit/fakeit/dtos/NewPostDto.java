package com.fakeit.fakeit.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class NewPostDto {
    private String titulo;
    private String urlImagen;
    private boolean real;
    private String usuarioId;
    private String grupoId;
}
