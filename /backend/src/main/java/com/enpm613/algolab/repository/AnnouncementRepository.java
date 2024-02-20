package com.enpm613.algolab.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.enpm613.algolab.entity.Announcement;
import com.enpm613.algolab.entity.Course;

public interface AnnouncementRepository  extends MongoRepository<Announcement, String>
{
    @Query("{courseId: ?0}")
    List<Announcement> findByCourse(String courseId);
}
