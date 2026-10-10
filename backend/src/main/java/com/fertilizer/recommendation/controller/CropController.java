package com.fertilizer.recommendation.controller;

import com.fertilizer.recommendation.entity.Crop;
import com.fertilizer.recommendation.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
@Tag(name = "Crop Management", description = "Endpoints to manage and retrieve crops and their agronomic requirements")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @GetMapping
    @Operation(summary = "Get list of all supported crops with optimal nutrient values")
    public ResponseEntity<List<Crop>> getAllCrops() {
        return ResponseEntity.ok(cropService.getAllCrops());
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get crop details and nutrient requirements by crop name")
    public ResponseEntity<Crop> getCropByName(@PathVariable String name) {
        return cropService.getCropByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Add a new crop with target N-P-K and pH ranges")
    public ResponseEntity<Crop> addCrop(@RequestBody Crop crop) {
        return ResponseEntity.ok(cropService.saveCrop(crop));
    }
}
