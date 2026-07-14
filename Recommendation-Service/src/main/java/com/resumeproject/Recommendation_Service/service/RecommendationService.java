package com.resumeproject.Recommendation_Service.service;

import com.resumeproject.Recommendation_Service.kafka.RecommendationEventPublisher;
import com.resumeproject.Recommendation_Service.model.Recommendation;
import com.resumeproject.Recommendation_Service.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final RecommendationRepository recommendationRepository;
    private final RecommendationEventPublisher recommendationEventPublisher;

    public RecommendationService(VectorStore vectorStore,
                                 ChatClient.Builder chatClientBuilder,
                                 RecommendationRepository recommendationRepository,
                                 RecommendationEventPublisher recommendationEventPublisher) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
        this.recommendationRepository = recommendationRepository;
        this.recommendationEventPublisher = recommendationEventPublisher;
    }

    public void generateRecommendation(String parsedResumeId,
                                       String candidateName,
                                       String candidateEmail,
                                       List<String> skills) {

        log.info("Generating recommendation for: {}", candidateEmail);

        String query = String.join(" ", skills);
        log.info("Searching ChromaDB with query: {}", query);

        List<Document> matchedDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .build()
        );

        log.info("Found {} matching jobs", matchedDocs.size());

        List<String> matchedJobs = matchedDocs.stream()
                .map(Document::getText)
                .collect(Collectors.toList());

        String prompt = """
                Candidate Profile:
                Name: %s
                Skills: %s
                
                Matched Jobs:
                %s
                
                Based on the candidate's skills, explain in 3-4 sentences why these jobs are a good match.
                Be specific about which skills match which job requirements.
                """.formatted(candidateName, query, String.join("\n", matchedJobs));

        log.info("Calling Ollama for explanation...");
        String explanation = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.info("Explanation generated: {}", explanation);

        Recommendation recommendation = new Recommendation();
        recommendation.setParsedResumeId(parsedResumeId);
        recommendation.setCandidateName(candidateName);
        recommendation.setCandidateEmail(candidateEmail);
        recommendation.setMatchJob(matchedJobs);
        recommendation.setExplanations(explanation);
        recommendation.setStatus("COMPLETED");

        Recommendation saved = recommendationRepository.save(recommendation);
        log.info("Recommendation saved with id: {}", saved.getId());

        recommendationEventPublisher.publishRecommendationsEvent(saved.getId());
    }
}