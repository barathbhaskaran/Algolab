package com.enpm613.algolab.entity.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PracticeQuestion {
    @Id
    private String id;

    private String questionDifficulty;

    private String questionName;

    private String questionLink;

    private String answerContent;

    public boolean isEmpty() {
        // Check if all relevant properties are null or empty
        return questionName.isEmpty();

    }


}
