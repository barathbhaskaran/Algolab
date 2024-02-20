package com.enpm613.algolab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {

    @Id
    private String id;
    private String title;
    private Difficulty difficulty;
    private String description;
    private User instructor;

    public enum Difficulty {
        EASY,
        AVERAGE,
        HARD
    }
}


