package com.fakeit.fakeit.dtos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class VoteDto {
    private String votoId;
    private String publicacionId;
    private String grupoId;
    private String usuarioId;
    private Boolean real;
    private Timestamp fechaHora;
}
