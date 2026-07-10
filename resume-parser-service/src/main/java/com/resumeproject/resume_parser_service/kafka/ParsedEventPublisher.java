package com.resumeproject.resume_parser_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class ParsedEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(ParsedEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "resume-parsed";

    public ParsedEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishResumeParsedEvent(String parsedResumeId) {
        log.info("Publishing event to topic: {} with parsedResumeId: {}", TOPIC, parsedResumeId);
        kafkaTemplate.send(TOPIC, parsedResumeId);
        log.info("Parsed Even published Successfully ");
    }
}
