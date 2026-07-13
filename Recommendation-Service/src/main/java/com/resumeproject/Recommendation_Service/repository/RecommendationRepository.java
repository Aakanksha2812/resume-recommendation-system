package com.resumeproject.Recommendation_Service.repository;

import com.resumeproject.Recommendation_Service.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
}
