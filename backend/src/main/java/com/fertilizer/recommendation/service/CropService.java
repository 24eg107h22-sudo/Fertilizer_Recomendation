package com.fertilizer.recommendation.service;

import com.fertilizer.recommendation.entity.Crop;
import com.fertilizer.recommendation.repository.CropRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CropService {

    private final CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    public Optional<Crop> getCropByName(String name) {
        return cropRepository.findByCropNameIgnoreCase(name);
    }

    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }
}
