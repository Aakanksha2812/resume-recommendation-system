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

      /* List<Document> jobDocs = List.of(
                new Document(
                        "Senior Java Developer at Razorpay. Requirements: 3+ years Java, " +
                                "Spring Boot, Microservices, REST APIs. Fintech domain preferred. Pune location.",
                        Map.of("company", "Razorpay", "role", "Senior Java Developer", "location", "Pune")
                ),
                new Document(
                        "Backend Engineer at PhonePe. Requirements: Java, Kafka, MongoDB, " +
                                "distributed systems, payment processing. 2+ years experience. Bangalore.",
                        Map.of("company", "PhonePe", "role", "Backend Engineer", "location", "Bangalore")
                ),
                new Document(
                        "Java Developer at Groww. Requirements: Spring Boot, MySQL, REST APIs, " +
                                "microservices. Fintech background preferred. 2-4 years experience.",
                        Map.of("company", "Groww", "role", "Java Developer", "location", "Bangalore")
                ),
                new Document(
                        "Software Engineer at Juspay. Requirements: Java, Kafka, payment gateway " +
                                "integration, ISO 20022, SWIFT formats. 2+ years experience.",
                        Map.of("company", "Juspay", "role", "Software Engineer", "location", "Bangalore")
                ),
                new Document(
                        "Backend Developer at CRED. Requirements: Java, Spring Boot, Redis, Kafka, " +
                                "high performance systems. 3+ years experience.",
                        Map.of("company", "CRED", "role", "Backend Developer", "location", "Bangalore")
                ),
                new Document(
                        "Java Microservices Developer at Zerodha. Requirements: Spring Cloud, " +
                                "Docker, Kubernetes, Kafka, MongoDB. 2+ years microservices experience.",
                        Map.of("company", "Zerodha", "role", "Java Microservices Developer", "location", "Bangalore")
                ),
                new Document(
                        "Senior Backend Engineer at Paytm. Requirements: Java, Spring Boot, " +
                                "Kafka, MySQL, MongoDB, payment systems. 3+ years experience.",
                        Map.of("company", "Paytm", "role", "Senior Backend Engineer", "location", "Noida")
                ),
                new Document(
                        "API Developer at Worldline. Requirements: Java, REST APIs, ISO 20022, " +
                                "Spring Boot, payment processing. 2+ years experience. Pune location.",
                        Map.of("company", "Worldline", "role", "API Developer", "location", "Pune")
                )
        );

        vectorStore.add(jobDocs);*/
        Document doc = new Document(
                "Java Spring Boot Developer",
                Map.of("company", "Test")
        );
        vectorStore.add(List.of(doc));
        log.info("Loaded {} job descriptions into ChromaDB");
        //, jobDocs.size());
    }
}