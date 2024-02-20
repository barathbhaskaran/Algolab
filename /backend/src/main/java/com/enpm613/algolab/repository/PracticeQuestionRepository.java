package com.enpm613.algolab.repository;
import com.enpm613.algolab.entity.lesson.PracticeQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PracticeQuestionRepository extends MongoRepository<PracticeQuestion, String> {
}
