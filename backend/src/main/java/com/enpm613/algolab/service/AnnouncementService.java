package com.enpm613.algolab.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enpm613.algolab.entity.Announcement;
import com.enpm613.algolab.entity.Course;
import com.enpm613.algolab.entity.CourseDTO;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.AnnouncementRepository;
import com.enpm613.algolab.repository.CourseRepository;

@Service
public class AnnouncementService {

    private final S3Service s3Service;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    CourseService courseService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AnnouncementService(S3Service s3Service,MongoTemplate mongoTemplate, AnnouncementRepository announcementRepository) {
        this.s3Service = s3Service;
        this.mongoTemplate = mongoTemplate;
        this.announcementRepository = announcementRepository;

    }
    
     public Announcement createAnnouncement(Announcement newAnnouncement, User instructor) throws IOException
    {
        Announcement announcement = new Announcement(null, newAnnouncement.getCourseId(),newAnnouncement.getTitle(), newAnnouncement.getContent());
        Announcement createdAnnouncement = announcementRepository.save(announcement);
        return createdAnnouncement;
    }

    public List<Announcement> getAnnouncementsbyCourse(String courseId){

        return announcementRepository.findByCourse(courseId);
    }

    public void deleteAnnouncement(String announcementId) throws IOException{
       announcementRepository.deleteById(announcementId);
    }

    
}
