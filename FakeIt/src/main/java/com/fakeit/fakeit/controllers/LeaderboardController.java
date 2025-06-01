package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.LeaderboardEntryDto;
import com.fakeit.fakeit.dtos.LeaderboardSummaryDto;
import com.fakeit.fakeit.facades.LeaderboardFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardFacade facade;

    @GetMapping
    public ResponseEntity<List<LeaderboardEntryDto>> global() {
        return ResponseEntity.ok(facade.global());
    }

    @GetMapping("/daily")
    public ResponseEntity<List<LeaderboardEntryDto>> daily(@RequestParam String grupoId) {
        return ResponseEntity.ok(facade.daily(grupoId));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<LeaderboardSummaryDto>> summary(@RequestParam String grupoId) {
        return ResponseEntity.ok(facade.summary(grupoId));
    }
}
