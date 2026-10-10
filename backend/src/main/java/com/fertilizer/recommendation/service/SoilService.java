package com.fertilizer.recommendation.service;

import com.fertilizer.recommendation.entity.SoilData;
import com.fertilizer.recommendation.repository.SoilDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoilService {

    private final SoilDataRepository soilDataRepository;

    public SoilService(SoilDataRepository soilDataRepository) {
        this.soilDataRepository = soilDataRepository;
    }

    public SoilData saveSoilData(SoilData soilData) {
        return soilDataRepository.save(soilData);
    }

    public List<SoilData> getSoilDataByUser(Long userId) {
        return soilDataRepository.findByUserIdOrderByTestedAtDesc(userId);
    }
}
