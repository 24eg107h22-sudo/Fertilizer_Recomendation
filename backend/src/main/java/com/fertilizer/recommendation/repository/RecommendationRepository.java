package com.fertilizer.recommendation.repository;

import com.fertilizer.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query("SELECT r FROM Recommendation r WHERE r.user.id = :userId ORDER BY r.createdDate DESC")
    List<Recommendation> findByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);

    List<Recommendation> findAllByOrderByCreatedDateDesc();
}
