package com.resumeproject.resume_parser_service.service;

import com.resumeproject.resume_parser_service.kafka.ParsedEventPublisher;
import com.resumeproject.resume_parser_service.model.ParsedResume;
import com.resumeproject.resume_parser_service.repository.ParsedResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ParsedService {

    private static final Logger log = LoggerFactory.getLogger(ParsedService.class);
    private final ParsedEventPublisher parsedEventPublisher;
    private final ParsedResumeRepository repository;

    public ParsedService(ParsedEventPublisher parsedEventPublisher, ParsedResumeRepository repository) {
        this.parsedEventPublisher = parsedEventPublisher;
        this.repository = repository;
    }

    public void parsedResume(String resumeId, String candidateName, String candidateEmail, String fileContent) {
        log.info("parsing resume for: {}", candidateEmail);
        List<String> extractedSkill = extractedSkill(fileContent);
        log.info("skill extracted from: {} ", extractedSkill);

        ParsedResume parsedResume = new ParsedResume();
        parsedResume.setResumeId(resumeId);
        parsedResume.setCandidateName(candidateName);
        parsedResume.setCandidateEmail(candidateEmail);
        parsedResume.setExtractedSkill(extractedSkill);
        parsedResume.setYearOfExp("2.9");
        parsedResume.setEducation("B.E Information Technology");
        parsedResume.setStatus("Parsed");
        ParsedResume saved = repository.save(parsedResume);
        log.info("Parsed Resume saved with {}", saved.getId());
        parsedEventPublisher.publishResumeParsedEvent(saved.getId());
    }

    private List<String> extractedSkill(String fileContent) {
        List<String> skills = new ArrayList<>();
        List<String> knownSkills = List.of("Java", "Spring Boot", "Kafka", "MongoDB",
                "MySQL", "Microservices", "Docker", "REST API",
                "Spring Cloud", "Kubernetes", "Python", "AWS");
        for (String skill : knownSkills) {
            if (fileContent != null && fileContent.toLowerCase().contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }
        return skills;
    }
}
