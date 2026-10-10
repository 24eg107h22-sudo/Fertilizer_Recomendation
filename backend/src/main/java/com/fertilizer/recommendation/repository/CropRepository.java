package com.fertilizer.recommendation.repository;

import com.fertilizer.recommendation.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findByCropNameIgnoreCase(String cropName);
}
