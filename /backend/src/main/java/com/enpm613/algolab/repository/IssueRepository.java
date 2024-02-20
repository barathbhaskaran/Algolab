package com.enpm613.algolab.repository;

import com.enpm613.algolab.entity.Issue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IssueRepository extends MongoRepository<Issue, String> {
}
