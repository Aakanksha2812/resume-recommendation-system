package com.resumeproject.Recommendation_Service.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.ai.vectorstore.SearchRequest;

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

        // Pehle check karo kya already loaded hai
        List<Document> existing = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("Java Developer")
                        .topK(1)
                        .build()
        );

        if (!existing.isEmpty()) {
            log.info("Job descriptions already loaded — skipping");
            return;
        }

        List<Document> jobDocs = List.of(
                new Document("Senior Java Developer at Razorpay. Requirements: 3 plus years Java Spring Boot Microservices REST APIs. Fintech domain preferred. Pune location."),
                new Document("Backend Engineer at PhonePe. Requirements: Java Kafka MongoDB distributed systems payment processing. 2 plus years experience. Bangalore."),
                new Document("Java Developer at Groww. Requirements: Spring Boot MySQL REST APIs microservices. Fintech background preferred. 2 to 4 years experience. Bangalore."),
                new Document("Software Engineer at Juspay. Requirements: Java Kafka payment gateway integration ISO 20022 SWIFT formats. 2 plus years experience. Bangalore."),
                new Document("Backend Developer at CRED. Requirements: Java Spring Boot Redis Kafka high performance systems. 3 plus years experience. Bangalore."),
                new Document("Java Microservices Developer at Zerodha. Requirements: Spring Cloud Docker Kubernetes Kafka MongoDB. 2 plus years microservices experience. Bangalore."),
                new Document("Senior Backend Engineer at Paytm. Requirements: Java Spring Boot Kafka MySQL MongoDB payment systems. 3 plus years experience. Noida."),
                new Document("API Developer at Worldline. Requirements: Java REST APIs ISO 20022 Spring Boot payment processing. 2 plus years experience. Pune.")
        );

        vectorStore.add(jobDocs);
        log.info("Loaded {} job descriptions into ChromaDB", jobDocs.size());
    }
}