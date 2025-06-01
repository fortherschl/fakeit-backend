package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.VoteDto;
import com.fakeit.fakeit.dtos.VoteRequestDto;
import com.fakeit.fakeit.facades.VoteFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
@RequiredArgsConstructor
public class VoteController {

    private final VoteFacade facade;

    @PostMapping
    public ResponseEntity<VoteDto> create(@RequestBody VoteRequestDto dto,
                                          @RequestParam String usuarioId) {
        return ResponseEntity.ok(facade.create(dto, usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<VoteDto>> list(@RequestParam String usuarioId,
                                              @RequestParam String grupoId) {
        return ResponseEntity.ok(facade.byUserAndGroup(usuarioId, grupoId));
    }
}
