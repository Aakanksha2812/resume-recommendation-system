package com.resumeproject.Recommendation_Service.consumer;

import com.resumeproject.Recommendation_Service.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RecommendationEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(RecommendationEventConsumer.class);
    private final RecommendationService recommendationService;

    public RecommendationEventConsumer(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }
@KafkaListener(topics = "resume-parsed",groupId = "recommendation-group")
    public void publishRecommendationEvent(String parsedResumeId) {
        log.info("consuming resume parsed event, recommnedationID: {}",parsedResumeId);
        recommendationService.generateRecommendation( parsedResumeId,
                "Aakanksha Shinde",
                "shindeaakanksha@gmail.com",
                List.of("Java", "Spring Boot", "Kafka", "MongoDB", "Microservices"));


    }
}
