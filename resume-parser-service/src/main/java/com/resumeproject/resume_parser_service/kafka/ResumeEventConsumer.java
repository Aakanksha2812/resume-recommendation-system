package com.resumeproject.resume_parser_service.kafka;

import com.resumeproject.resume_parser_service.service.ParsedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class ResumeEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(ResumeEventConsumer.class);
    private final ParsedService parsedService;

    public ResumeEventConsumer(ParsedService parsedService) {
        this.parsedService = parsedService;
    }

    @KafkaListener(topics = "resume-uploaded", groupId = "parser-group")
    public void consumeResumeUploadEvent(String resumeId) {
        log.info("recevied event from topic resume-uploaded, resumeId: {}",resumeId);
        parsedService.parsedResume(resumeId,"Aakanksha Shinde","shinde@gmail.com","Java Spring Boot Kafka MongoDB Microservices Docker");
    }
}
