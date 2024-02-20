package com.enpm613.algolab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseDTO {

    @Id
    private String id;
    private String title;
    private MultipartFile image;
    private Course.Difficulty difficulty;
    private String description;

    public enum Difficulty {
        EASY,
        AVERAGE,
        HARD
    }
}
