package com.enpm613.algolab.repository;

import com.enpm613.algolab.entity.Feedback;
import com.enpm613.algolab.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {

    @Query("{courseId: ?0}")
    List<Feedback> findByCourse(String courseId);

    @Query("{instructor: ?0}")
    List<Feedback> findByInstructor(User instructor);

}