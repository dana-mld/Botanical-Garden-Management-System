package com.example.demo.controller;

import com.example.demo.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllStatistics() {
        return ResponseEntity.ok(statisticsService.getAllStatistics());
    }

    @GetMapping("/distribution-by-type")
    public ResponseEntity<Map<String, Object>> getDistributionByType() {
        return ResponseEntity.ok(statisticsService.getDistributionByType());
    }

    @GetMapping("/carnivorous-stats")
    public ResponseEntity<Map<String, Object>> getCarnivorousStats() {
        return ResponseEntity.ok(statisticsService.getCarnivorousVsNonCarnivorous());
    }

    @GetMapping("/top-species")
    public ResponseEntity<Map<String, Object>> getTopSpecies(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(statisticsService.getTopSpecies(limit));
    }
}