package com.fakeit.fakeit.dtos;

import lombok.Data;
import java.util.List;
import com.google.cloud.Timestamp;

@Data
public class GroupDto {
    private String grupoId;
    private String nombreGrupo;
    private String descripcion;
    private boolean publico;
    private int maximoUsuarios;
    private Timestamp fechaCreacion;
    private Timestamp fechaUltimoMensaje;
    private String fotoPerfil;
    private List<String> administradores;
    private List<String> usuarios;
    private int cantidadUsuarios;
    private int rondaActual;
}
