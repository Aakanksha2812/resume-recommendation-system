package com.resumeproject.resume_service.controller;

import com.resumeproject.resume_service.model.Resume;
import com.resumeproject.resume_service.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Resume> uploadResume(
            @RequestParam String candidateName,
            @RequestParam String candidateEmail,
            @RequestParam String fileName,
            @RequestParam String fileContent) {
        log.info("Received resume upload request for: {}", candidateEmail);
        Resume resume = resumeService.uploadResume(candidateName, candidateEmail, fileName, fileContent);
        return ResponseEntity.ok(resume);
    }
}