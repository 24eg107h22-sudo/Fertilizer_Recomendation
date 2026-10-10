package com.fertilizer.recommendation.repository;

import com.fertilizer.recommendation.entity.SoilData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoilDataRepository extends JpaRepository<SoilData, Long> {

    @Query("SELECT s FROM SoilData s WHERE s.user.id = :userId ORDER BY s.testedAt DESC")
    List<SoilData> findByUserIdOrderByTestedAtDesc(@Param("userId") Long userId);
}
