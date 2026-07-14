package com.resumeproject.resume_parser_service.kafka;

import com.resumeproject.resume_parser_service.service.ParsedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.bson.Document;

@Service
public class ResumeEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ResumeEventConsumer.class);
    private final ParsedService parsedService;
    private final MongoTemplate mongoTemplate;

    public ResumeEventConsumer(ParsedService parsedService, MongoTemplate mongoTemplate) {
        this.parsedService = parsedService;
        this.mongoTemplate = mongoTemplate;
    }

    @KafkaListener(topics = "resume-uploaded", groupId = "parser-group")
    public void consumeResumeUploadEvent(String resumeId) {
        log.info("Received event from topic resume-uploaded, resumeId: {}", resumeId);

        // MongoDB se actual resume fetch karo
        Query query = new Query(Criteria.where("_id").is(resumeId));
        Document resume = mongoTemplate.findOne(query, Document.class, "resumes");

        if (resume == null) {
            log.error("Resume not found for id: {}", resumeId);
            return;
        }

        String candidateName = resume.getString("candidateName");
        String candidateEmail = resume.getString("candidateEmail");
        String fileContent = resume.getString("fileContent");

        log.info("Fetched resume for: {}", candidateEmail);

        parsedService.parsedResume(resumeId, candidateName, candidateEmail, fileContent);
    }
}