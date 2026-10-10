package com.fertilizer.recommendation.service;

import com.fertilizer.recommendation.entity.Recommendation;
import com.fertilizer.recommendation.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    private final RecommendationRepository recommendationRepository;

    public HistoryService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public List<Recommendation> getRecommendationsByUserId(Long userId) {
        return recommendationRepository.findByUserIdOrderByCreatedDateDesc(userId);
    }

    public List<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAllByOrderByCreatedDateDesc();
    }

    public Optional<Recommendation> getRecommendationById(Long id) {
        return recommendationRepository.findById(id);
    }

    public boolean deleteRecommendation(Long id) {
        if (recommendationRepository.existsById(id)) {
            recommendationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
