package com.enpm613.algolab.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Announcement {
    
    @Id
    private String id;
    private String courseId;
    private String title;
    private String content;
    
}
