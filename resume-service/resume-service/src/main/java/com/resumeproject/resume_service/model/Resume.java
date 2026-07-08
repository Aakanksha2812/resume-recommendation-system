package com.resumeproject.resume_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "resumes")
public class Resume {
    @Id
    private String id;

    private String candidateName;
    private String candidateEmail;
    private String fileName;
    private String fileContent;
    private String status;

}
