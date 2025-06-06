package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.NotificationDto;
import com.fakeit.fakeit.dtos.NotificationResponseDto;
import com.fakeit.fakeit.facades.NotificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationFacade facade;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(facade.getPending(userId));
    }

    @PostMapping("/{id}/responder")
    public ResponseEntity<String> respond(
            @PathVariable String id,
            @RequestBody NotificationResponseDto dto
    ) {
        String result = facade.respond(id, dto.isAccept());
        return ResponseEntity.ok(result);
    }
}

