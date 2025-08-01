package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.AIRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIRecommendationRepository extends JpaRepository<AIRecommendation, Long> {
}
