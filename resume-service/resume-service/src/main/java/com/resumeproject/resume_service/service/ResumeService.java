package com.resumeproject.resume_service.service;

import com.resumeproject.resume_service.kafka.ResumeEventPublisher;
import com.resumeproject.resume_service.model.Resume;
import com.resumeproject.resume_service.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);
    private final ResumeRepository resumeRepository;
    private final ResumeEventPublisher resumeEventPublisher;

    public ResumeService(ResumeRepository resumeRepository, ResumeEventPublisher resumeEventPublisher) {
        this.resumeRepository = resumeRepository;
        this.resumeEventPublisher = resumeEventPublisher;
    }

    public Resume uploadResume(String candidateName,
                               String candidateEmail,
                               String fileName,
                               String fileContent) {
        Resume resume = Resume.builder()
                .candidateName(candidateName)
                .candidateEmail(candidateEmail)
                .fileName(fileName)
                .fileContent(fileContent)
                .status("UPLOADED")
                .build();

        Resume savedResume = resumeRepository.save(resume);
        log.info("Resume saved to MongoDB with id: {}", savedResume.getId());
        resumeEventPublisher.publishResumeUploadEvent(savedResume.getId());
        return savedResume;
    }
}