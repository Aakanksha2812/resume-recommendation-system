package com.resumeproject.resume_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResumeEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ResumeEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "resume-uploaded";

    public ResumeEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishResumeUploadEvent(String resumeId) {
        log.info("Publishing event to topic: {} with resumeId: {}", TOPIC, resumeId);
        kafkaTemplate.send(TOPIC, resumeId);
        log.info("Event published successfully");
    }
}