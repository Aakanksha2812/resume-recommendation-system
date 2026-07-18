package com.resumeproject.resume_parser_service.kafka;

import com.resumeproject.resume_parser_service.service.ParsedService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

        String cleanId = resumeId.replace("\"", "").trim();
        log.info("Received event from topic resume-uploaded, resumeId: {}", cleanId);


        Query query = new Query(Criteria.where("_id").is(new ObjectId(cleanId)));
        Document resume = mongoTemplate.findOne(query, Document.class, "resumes");

        if (resume == null) {
            log.error("Resume not found for id: {}", cleanId);
            return;
        }

        String candidateName = resume.getString("candidateName");
        String candidateEmail = resume.getString("candidateEmail");
        String fileContent = resume.getString("fileContent");

        log.info("Fetched resume for: {} with content: {}", candidateEmail, fileContent);
        parsedService.parsedResume(cleanId, candidateName, candidateEmail, fileContent);
    }
}