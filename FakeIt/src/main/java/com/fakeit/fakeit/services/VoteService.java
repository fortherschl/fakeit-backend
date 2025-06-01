package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.VoteDto;
import com.fakeit.fakeit.dtos.VoteRequestDto;

import java.util.List;

public interface VoteService {

    VoteDto addVote(VoteRequestDto dto, String usuarioId);

    List<VoteDto> getVotesByUserAndGroup(String usuarioId, String grupoId);
}
