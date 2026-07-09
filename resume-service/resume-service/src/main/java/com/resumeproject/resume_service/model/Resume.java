package com.resumeproject.resume_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resumes")
public class Resume {

    @Id
    private String id;
    private String candidateName;
    private String candidateEmail;
    private String fileName;
    private String fileContent;
    private String status;

    public Resume() {}

    public Resume(String id, String candidateName, String candidateEmail, String fileName, String fileContent, String status) {
        this.id = id;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.status = status;
    }

    public static Builder builder() { return new Builder(); }

    public String getId() { return id; }
    public String getCandidateName() { return candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public String getFileName() { return fileName; }
    public String getFileContent() { return fileContent; }
    public String getStatus() { return status; }

    public static class Builder {
        private String id, candidateName, candidateEmail, fileName, fileContent, status;
        public Builder candidateName(String v) { this.candidateName = v; return this; }
        public Builder candidateEmail(String v) { this.candidateEmail = v; return this; }
        public Builder fileName(String v) { this.fileName = v; return this; }
        public Builder fileContent(String v) { this.fileContent = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Resume build() { return new Resume(null, candidateName, candidateEmail, fileName, fileContent, status); }
    }
}