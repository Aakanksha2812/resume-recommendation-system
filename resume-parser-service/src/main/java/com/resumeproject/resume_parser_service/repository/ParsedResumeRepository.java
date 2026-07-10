package com.resumeproject.resume_parser_service.repository;

import com.resumeproject.resume_parser_service.model.ParsedResume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsedResumeRepository extends MongoRepository<ParsedResume, String> {
}
