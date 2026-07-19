package com.resumeproject.resume_service.controller;

import com.resumeproject.resume_service.model.Resume;
import com.resumeproject.resume_service.service.ResumeService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            @RequestParam MultipartFile file) {
        log.info("Received resume upload request for: {}", candidateEmail);
        String fileContent = "";
        try {
            log.info("extracting details from pdf");
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            fileContent = stripper.getText(document);
            document.close();
            log.info("pdf text added file content {}", fileContent.length());
        } catch (IOException io) {
            log.error("error while extracting text from pdf  {}", io.getMessage());
            return ResponseEntity.badRequest().build();
        }
        Resume resume = resumeService.uploadResume(candidateName, candidateEmail, fileName, fileContent);
        return ResponseEntity.ok(resume);
    }
}