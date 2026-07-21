package com.resumeproject.Recommendation_Service.controller;

import com.resumeproject.Recommendation_Service.model.Recommendation;
import com.resumeproject.Recommendation_Service.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/recommendations")
public class RecommendationController {

    private static final Logger log = LoggerFactory.getLogger(RecommendationController.class);
    private final RecommendationRepository recommendationRepository;

    public RecommendationController(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }


    @GetMapping("/{email}")
    public ResponseEntity<List<Recommendation>> getRecommendations(@PathVariable String email) {
        log.info("Fetching recommendations for: {}", email);
        List<Recommendation> recommendations = recommendationRepository.findByCandidateEmail(email);
        return ResponseEntity.ok(recommendations);
    }


    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendations() {
        List<Recommendation> recommendations = recommendationRepository.findAll();
        return ResponseEntity.ok(recommendations);
    }
}