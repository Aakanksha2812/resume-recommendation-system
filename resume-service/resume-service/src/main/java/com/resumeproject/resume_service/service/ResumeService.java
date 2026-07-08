package com.resumeproject.resume_service.service;

import com.resumeproject.resume_service.kafka.ResumeEventPublisher;
import com.resumeproject.resume_service.model.Resume;
import com.resumeproject.resume_service.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {
    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);
    private final ResumeRepository repo;
    private final ResumeEventPublisher resumeEventPublisher;

    public Resume uploadResume(String candidateName, String candidateEmail, String fileName, String fileContent) {
        Resume resume = Resume.builder().candidateName(candidateName).candidateEmail(candidateEmail).fileName(fileName).fileContent(fileName).status("UPLOADED").build();
        Resume savedResume = repo.save(resume);
        log.info("Resume saved to MongoDB with id: {}", savedResume.getId());


        resumeEventPublisher.publishResumeUploadEvent(savedResume.getId());

        return savedResume;
    }
}
