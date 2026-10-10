package com.fertilizer.recommendation.controller;

import com.fertilizer.recommendation.entity.SoilData;
import com.fertilizer.recommendation.service.SoilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soil")
@Tag(name = "Soil Diagnostic Data", description = "Endpoints to record and retrieve soil test readings")
public class SoilController {

    private final SoilService soilService;

    public SoilController(SoilService soilService) {
        this.soilService = soilService;
    }

    @PostMapping
    @Operation(summary = "Save fresh soil test data (N, P, K, pH, moisture, temperature)")
    public ResponseEntity<SoilData> saveSoilData(@RequestBody SoilData soilData) {
        return ResponseEntity.ok(soilService.saveSoilData(soilData));
    }

    @GetMapping("/farmer/{userId}")
    @Operation(summary = "Get historical soil test readings for a farmer")
    public ResponseEntity<List<SoilData>> getSoilDataByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(soilService.getSoilDataByUser(userId));
    }
}
