package com.resumeproject.Recommendation_Service.consumer;

import com.resumeproject.Recommendation_Service.service.RecommendationService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(RecommendationEventConsumer.class);
    private final RecommendationService recommendationService;
    private final MongoTemplate mongoTemplate;

    public RecommendationEventConsumer(RecommendationService recommendationService,
                                       MongoTemplate mongoTemplate) {
        this.recommendationService = recommendationService;
        this.mongoTemplate = mongoTemplate;
    }

    @KafkaListener(topics = "resume-parsed", groupId = "recommendation-group")
    public void consumeResumeParsedEvent(String parsedResumeId) {


        String cleanId = parsedResumeId.replace("\"", "").trim();
        log.info("Received event from topic resume-parsed, parsedResumeId: {}", cleanId);


        Query query = new Query(Criteria.where("_id").is(new ObjectId(cleanId)));
        Document parsedResume = mongoTemplate.findOne(query, Document.class, "parsed_resumes");

        if (parsedResume == null) {
            log.error("ParsedResume not found for id: {}", cleanId);
            return;
        }

        String candidateName = parsedResume.getString("candidateName");
        String candidateEmail = parsedResume.getString("candidateEmail");
        List<?> skillsRaw = (List<?>) parsedResume.get("extractedSkill");
        List<String> skills = skillsRaw.stream()
                .map(Object::toString)
                .toList();

        log.info("Fetched parsed resume for: {} with skills: {}", candidateEmail, skills);

        recommendationService.generateRecommendation(
                cleanId,
                candidateName,
                candidateEmail,
                skills
        );
    }
}