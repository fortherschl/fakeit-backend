package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.*;
import com.fakeit.fakeit.facades.GroupFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
@RequiredArgsConstructor
public class GroupController {

    private final GroupFacade facade;

    @PostMapping
    public ResponseEntity<GroupDto> create(@RequestBody GroupCreateDto dto,
                                           @RequestParam String creatorId) {
        return ResponseEntity.ok(facade.create(dto, creatorId));
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<GroupDto>> myGroups(@RequestParam String userId) {
        return ResponseEntity.ok(facade.groupsOfUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>>    notIn(@RequestParam String userId) {
        return ResponseEntity.ok(facade.groupsNotIn(userId));
    }

    @GetMapping("/{grupoId}")
    public ResponseEntity<GroupDto> get(@PathVariable String grupoId) {
        GroupDto g = facade.get(grupoId);
        return g != null ? ResponseEntity.ok(g) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{grupoId}/detalles")
    public ResponseEntity<GroupDetailsDto> details(@PathVariable String grupoId,
                                                   @RequestParam String requesterId) {
        GroupDetailsDto d = facade.details(grupoId, requesterId);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{grupoId}/unirse")
    public ResponseEntity<GroupDto> join(@PathVariable String grupoId,
                                         @RequestParam String userId) {
        return ResponseEntity.ok(facade.join(grupoId, userId));
    }

    @PostMapping("/{grupoId}/salir")
    public ResponseEntity<Void> leave(@PathVariable String grupoId,
                                      @RequestParam String userId) {
        facade.leave(grupoId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{grupoId}/invitar")
    public ResponseEntity<Void> invite(@PathVariable String grupoId,
                                       @RequestParam String adminId,
                                       @RequestBody GroupInviteDto body) {
        facade.invite(grupoId, adminId, body.getInvitedUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{grupoId}/solicitar-ingreso")
    public ResponseEntity<Void> requestJoin(@PathVariable String grupoId,
                                            @RequestParam String userId) {
        facade.requestJoin(grupoId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{grupoId}/expulsar")
    public ResponseEntity<Void> remove(@PathVariable String grupoId,
                                       @RequestParam String adminId,
                                       @RequestParam String usuarioId) {
        facade.remove(grupoId, adminId, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{grupoId}")
    public ResponseEntity<Void> delete(@PathVariable String grupoId,
                                       @RequestParam String adminId) {
        facade.delete(grupoId, adminId);
        return ResponseEntity.ok().build();
    }
}
