package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.LeaderboardEntryDto;
import com.fakeit.fakeit.dtos.LeaderboardSummaryDto;

import java.util.List;

public interface LeaderboardService {

    List<LeaderboardEntryDto> getGlobalLeaderboard();

    List<LeaderboardEntryDto> getDailyLeaderboard(String grupoId);

    List<LeaderboardSummaryDto> getSummary(String grupoId);
}
