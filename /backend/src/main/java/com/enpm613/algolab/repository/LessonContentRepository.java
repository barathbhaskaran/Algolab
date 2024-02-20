package com.enpm613.algolab.repository;
import com.enpm613.algolab.entity.lesson.LessonContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface LessonContentRepository extends MongoRepository<LessonContent, String> {

    @Query("{id: ?0}")
    Optional<LessonContent> findById(String id);
}

