package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.services.batch.LeaderboardBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev-tools")
@RequiredArgsConstructor
public class DevToolsController {

    private final LeaderboardBatchService batch;

    @PostMapping("/process-leaderboard")
    public ResponseEntity<String> process() {
        try {
            batch.doProcess();
            return ResponseEntity.ok("Leaderboard procesado manualmente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
}
