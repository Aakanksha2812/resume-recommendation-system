package com.resumeproject.Recommendation_Service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection  = "recommendations")
public class Recommendation {
    @Id
    private String id;

    private String parsedResumeId;
    private String candidateName;
    private String candidateEmail;
    private List<String> matchJob;
    private String explanations;
    private String status;

    public Recommendation() {
    }

    public Recommendation(String id, String parsedResumeId, String candidateName, String candidateEmail, List<String> matchJob, String explanations, String status) {
        this.id = id;
        this.parsedResumeId = parsedResumeId;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.matchJob = matchJob;
        this.explanations = explanations;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParsedResumeId() {
        return parsedResumeId;
    }

    public void setParsedResumeId(String parsedResumeId) {
        this.parsedResumeId = parsedResumeId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public List<String> getMatchJob() {
        return matchJob;
    }

    public void setMatchJob(List<String> matchJob) {
        this.matchJob = matchJob;
    }

    public String getExplanations() {
        return explanations;
    }

    public void setExplanations(String explanations) {
        this.explanations = explanations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
