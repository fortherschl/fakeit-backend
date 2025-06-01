package com.fakeit.fakeit.dtos;

import lombok.Data;

@Data
public class LeaderboardSummaryDto {
    private String usuarioId;
    private String nombreUsuario;
    private int puntosTotales;
    private String rango;
}
