package com.resumeproject.Recommendation_Service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecommendationEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(RecommendationEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String TOPIC = "recommendation-ready";

    public RecommendationEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRecommendationsEvent(String recommendationId) {
        log.info("Publishing event to topic: {} with recommendationId: {}", TOPIC, recommendationId);
        kafkaTemplate.send(TOPIC, recommendationId);
        log.info("Recommendation event published successfully");
    }

}
