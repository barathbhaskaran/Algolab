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
public class Feedback {

    @Id
    private String id;
    private User user;
    private Course course;
    private User instructor;
    private String content;

    public Feedback(User user, Course course, User instructor, String content) {
        this.user = user;
        this.course =course;
        this.instructor = instructor;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    
}
