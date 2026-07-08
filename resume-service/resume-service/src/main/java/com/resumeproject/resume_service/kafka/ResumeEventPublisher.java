package com.resumeproject.resume_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "resume-uploaded";

    public void publishResumeUploadEvent(String resumeId) {
        log.info("Publishing event to topic: {} with resumeId: {}", TOPIC, resumeId);
        kafkaTemplate.send(TOPIC, resumeId);
        log.info("Event published successfully");
    }
}