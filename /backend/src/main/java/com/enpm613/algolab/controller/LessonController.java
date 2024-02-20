package com.enpm613.algolab.controller;


import com.enpm613.algolab.entity.lesson.LessonPage;
import com.enpm613.algolab.entity.lesson.PracticeQuestion;
import com.enpm613.algolab.service.LessonService;
import com.enpm613.algolab.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@CrossOrigin("*")
public class LessonController {

    @Autowired
    LessonService lessonService;
    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

    @GetMapping("/getLesson")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT','ADMIN')")
    public ResponseEntity<Object> getLesson(@RequestParam("lessonId") String id) {
        try {
            logger.debug("Inside get+++");
            return ResponseEntity.ok(lessonService.getLesson(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/createLesson")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    public ResponseEntity<Object> createLesson(@RequestBody LessonPage lessonPage) {
        try {
            return ResponseEntity.ok(lessonService.createLesson(lessonPage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/updateLesson")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    public ResponseEntity<Object> updateLesson(@RequestBody LessonPage lessonPage) {
        try {
            return ResponseEntity.ok(lessonService.updateLesson(lessonPage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteLesson")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    public ResponseEntity<Object> deleteLesson(@RequestParam("lessonId") String id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.ok("Lesson deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    @GetMapping("/getPracticeQuestion/{practiceQuestionId}")
//    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
//    public ResponseEntity<Object> getPracticeQuestion(@PathVariable("practiceQuestionId") String id) {
//        try {
//            return ResponseEntity.ok(lessonService.getPracticeQuestion(id));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

//    @PostMapping("/createPracticeQuestion")
//    @PreAuthorize("hasAuthority('INSTRUCTOR')")
//    public ResponseEntity<Object> createPracticeQuestion(@RequestBody PracticeQuestion practiceQuestion) {
//        try {
//            return ResponseEntity.ok(lessonService.createPracticeQuestion(practiceQuestion));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }


//    @PutMapping("/updatePracticeQuestion")
//    @PreAuthorize("hasAuthority('INSTRUCTOR')")
//    public ResponseEntity<Object> updatePracticeQuestion(@RequestBody PracticeQuestion practiceQuestion) {
//        try {
//            return ResponseEntity.ok(lessonService.updatePracticeQuestion(practiceQuestion));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

//    @DeleteMapping("/deletePracticeQuestion/{deletePracticeQuestionId}")
//    @PreAuthorize("hasAuthority('INSTRUCTOR')")
//    public ResponseEntity<Object> deletePracticeQuestion(@PathVariable("practiceQuestionId") Long id) {
//        try {
//            lessonService.deletePracticeQuestion(id);
//            return ResponseEntity.ok("Practice Question deleted successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

    @GetMapping("/getLessonContent/{lessonId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT','ADMIN')")
    public ResponseEntity<Object> getLessonContent(@PathVariable("lessonId") String id) {
        try {
            logger.debug("Inside get+++");
            return ResponseEntity.ok(lessonService.getLessonContent(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getLessonPages/{courseId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT','ADMIN')")
    public ResponseEntity<Object> getLessonPage(@PathVariable("courseId") String id) {
        try {
            logger.debug("Inside get+++");
            return ResponseEntity.ok(lessonService.getLessonPages(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


}
