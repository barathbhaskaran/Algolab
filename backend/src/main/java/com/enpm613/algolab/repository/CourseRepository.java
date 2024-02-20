package com.enpm613.algolab.repository;

import com.enpm613.algolab.entity.Course;
import com.enpm613.algolab.entity.Feedback;
import com.enpm613.algolab.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {

    List<Course> findByInstructorId(String instructorId);

    @Query("{_id: ?0}")
    Course findByCourseId(String courseId);

    @Query("{title: ?0}")
    Course findByTitle(String title);
}
