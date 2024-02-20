package com.enpm613.algolab.service;

import com.enpm613.algolab.entity.Course;
import com.enpm613.algolab.entity.CourseDTO;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class CourseService {

    private final S3Service s3Service;

    @Autowired
    CourseRepository courseRepository;


    private final MongoTemplate mongoTemplate;

    @Autowired
    public CourseService(S3Service s3Service,MongoTemplate mongoTemplate) {
        this.s3Service = s3Service;
        this.mongoTemplate = mongoTemplate;

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void uploadImage(String imageName, File imageFile) {
        // Use the configured bucket name and upload the image to S3
        s3Service.uploadFileToS3( imageName, imageFile);
    }

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    public List<Course> getUserCourse(User instructor){
        return courseRepository.findByInstructorId(instructor.getId());
    }

    public Course getCourseById(String courseId){
        return courseRepository.findById(courseId).get();
    }

    public Course createCourse(CourseDTO newCourse, User instructor) throws IOException{

        Course existingCourse = courseRepository.findByTitle(newCourse.getTitle());
        if(existingCourse != (null)){
            throw new RuntimeException("Course already exists");
        }
        MultipartFile imageFile = newCourse.getImage();
        Course course = new Course(null,newCourse.getTitle(),newCourse.getDifficulty(),newCourse.getDescription(),instructor);
        Course createdCourse = courseRepository.save(course);
        File file = convertMultiPartToFile(imageFile);
        uploadImage(createdCourse.getId(),file);
        file.delete();
        return createdCourse;

    }


    public void deleteCourse(String courseId) throws IOException{
        courseRepository.deleteById(courseId);
//        s3Service.deleteFile(courseId);
    }

    public boolean checkCourse(User curUser, String courseId) throws IOException{
        Course curCourse = courseRepository.findById(courseId).get();
        return curCourse.getInstructor().getId().equals(curUser.getId());
    }



}
