package com.resumeproject.resume_service.repository;

import com.resumeproject.resume_service.model.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository  extends MongoRepository<Resume,String> {

}
