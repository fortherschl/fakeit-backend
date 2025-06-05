package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.NotificationDto;
import com.fakeit.fakeit.dtos.NotificationResponseDto;
import com.fakeit.fakeit.facades.NotificationFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getMyNotifications_returnsList() throws Exception {
        NotificationDto noti = new NotificationDto();
        noti.setNotificacionId("123");
        noti.setTipo("invitacion_grupo");
        noti.setGrupoId("grupo1");
        noti.setRemitenteId("user1");
        noti.setDestinatarioId("testUser");
        noti.setEstado("pendiente");
        noti.setRemitenteUsername("Remitente");
        noti.setGrupoNombre("Grupo A");

        Mockito.when(facade.getPending("testUser"))
                .thenReturn(List.of(noti));

        mockMvc.perform(get("/notificaciones")
                        .principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificacionId").value("123"))
                .andExpect(jsonPath("$[0].grupoNombre").value("Grupo A"));
    }

    @Test
    void respond_acceptNotification_returnsOk() throws Exception {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setAccept(true);

        Mockito.when(facade.respond("notif123", "testUser", true))
                .thenReturn("Aceptada");

        mockMvc.perform(post("/notificaciones/notif123/responder")
                        .principal(() -> "testUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Aceptada"));
    }
}
