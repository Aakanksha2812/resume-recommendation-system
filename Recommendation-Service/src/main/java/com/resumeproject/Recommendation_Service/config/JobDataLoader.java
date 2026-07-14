package com.resumeproject.Recommendation_Service.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JobDataLoader {

    private static final Logger log = LoggerFactory.getLogger(JobDataLoader.class);
    private final VectorStore vectorStore;

    public JobDataLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadJobDescriptions() {
        log.info("Loading job descriptions into ChromaDB...");

        List<Document> jobDocs = List.of(
                new Document(
                        "Senior Java Developer at Razorpay. Requirements: 3 plus years Java Spring Boot Microservices REST APIs. Fintech domain preferred. Pune location."
                ),
                new Document(
                        "Backend Engineer at PhonePe. Requirements: Java Kafka MongoDB distributed systems payment processing. 2 plus years experience. Bangalore."
                )
        );

        vectorStore.add(jobDocs);
        log.info("Successfully loaded job descriptions into ChromaDB");
    }
}