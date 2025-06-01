package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.LeaderboardEntryDto;
import com.fakeit.fakeit.dtos.LeaderboardSummaryDto;
import com.fakeit.fakeit.services.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LeaderboardFacade {

    private final LeaderboardService service;

    public List<LeaderboardEntryDto> global() {
        return service.getGlobalLeaderboard();
    }

    public List<LeaderboardEntryDto> daily(String grupoId) {
        return service.getDailyLeaderboard(grupoId);
    }

    public List<LeaderboardSummaryDto> summary(String grupoId) {
        return service.getSummary(grupoId);
    }
}
