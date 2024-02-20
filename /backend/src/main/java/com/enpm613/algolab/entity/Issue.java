package com.enpm613.algolab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Issue {
    @Id
    String id;
    private User user;
    private Severity severity;
    private String name;
    private String description;
    public enum Severity {
        CRITICAL,
        MODERATE,
        MINOR
    }
}
