package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.VoteDto;
import com.fakeit.fakeit.dtos.VoteRequestDto;
import com.fakeit.fakeit.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VoteFacade {

    private final VoteService service;

    public VoteDto create(VoteRequestDto dto, String usuarioId) {
        return service.addVote(dto, usuarioId);
    }

    public List<VoteDto> byUserAndGroup(String usuarioId, String grupoId) {
        return service.getVotesByUserAndGroup(usuarioId, grupoId);
    }
}
