package com.resumeproject.Recommendation_Service.controller;

import com.resumeproject.Recommendation_Service.kafka.RecommendationEventPublisher;
import com.resumeproject.Recommendation_Service.model.Recommendation;
import com.resumeproject.Recommendation_Service.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private static final Logger log = LoggerFactory.getLogger(RecommendationEventPublisher.class);

    private final RecommendationRepository recommendationRepository;

    public RecommendationController(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @GetMapping("/email")
    public ResponseEntity<List<Recommendation>> getRecommendation(@PathVariable String email) {
        log.info("fetching recommendation email {} ", email);
        List<Recommendation> recommendations = recommendationRepository.findByCandidateEmail(email);
        return ResponseEntity.ok(recommendations);

    }

    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendation(@PathVariable String email) {
        List<Recommendation> recommendations = recommendationRepository.findAll();
        return ResponseEntity.ok(recommendations);
    }
}
