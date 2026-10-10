package com.fertilizer.recommendation.controller;

import com.fertilizer.recommendation.dto.RecommendationRequest;
import com.fertilizer.recommendation.dto.RecommendationResponse;
import com.fertilizer.recommendation.entity.Recommendation;
import com.fertilizer.recommendation.service.HistoryService;
import com.fertilizer.recommendation.service.RecommendationEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "Fertilizer Recommendations", description = "Endpoints for generating and reviewing agronomic fertilizer recommendations")
public class RecommendationController {

    private final RecommendationEngineService recommendationEngineService;
    private final HistoryService historyService;

    public RecommendationController(RecommendationEngineService recommendationEngineService,
                                    HistoryService historyService) {
        this.recommendationEngineService = recommendationEngineService;
        this.historyService = historyService;
    }

    @PostMapping("/generate")
    @Operation(summary = "Analyze soil and crop data and generate fertilizer recommendation")
    public ResponseEntity<RecommendationResponse> generateRecommendation(@Valid @RequestBody RecommendationRequest request) {
        RecommendationResponse response = recommendationEngineService.generateRecommendation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(summary = "Get previous recommendation history, optionally filtered by userId")
    public ResponseEntity<List<Recommendation>> getHistory(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return ResponseEntity.ok(historyService.getRecommendationsByUserId(userId));
        }
        return ResponseEntity.ok(historyService.getAllRecommendations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recommendation details by recommendation ID")
    public ResponseEntity<Recommendation> getById(@PathVariable Long id) {
        return historyService.getRecommendationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recommendation history entry by ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean deleted = historyService.deleteRecommendation(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
