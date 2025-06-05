package com.fakeit.fakeit.dtos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class NotificationDto {
    private String notificacionId;
    private String tipo;
    private String grupoId;
    private String remitenteId;
    private String destinatarioId;
    private String estado;
    private Timestamp creadoEn;
    private Timestamp respondidoEn;

    private String remitenteUsername;
    private String grupoNombre;
}
