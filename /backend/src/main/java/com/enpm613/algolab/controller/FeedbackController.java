package com.enpm613.algolab.controller;

import com.enpm613.algolab.entity.Feedback;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.FeedbackRepository;
import com.enpm613.algolab.service.FeedbackService;
import com.enpm613.algolab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/feedback")
@CrossOrigin("*")
public class FeedbackController {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    public FeedbackController(FeedbackRepository feedbackRepository, FeedbackService feedbackService) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackService = feedbackService;
    }

    @PostMapping("/add/{courseId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @ResponseBody
    public ResponseEntity<Object> addFeedback(@AuthenticationPrincipal UserDetails user, @RequestBody Feedback feedback, @PathVariable String courseId){
        try {
            String curUsername = user.getUsername();
            User curUser = userService.getUserByUsername(curUsername);
            Feedback savedFeedback = feedbackService.addFeedback(feedback, curUser, courseId);
            return ResponseEntity.ok("true");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/viewAll")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseBody
    public List<Feedback> getAllFeedback(@AuthenticationPrincipal UserDetails user) {
        List<Feedback> feedbackList = feedbackService.viewFeedback();
        return feedbackList;
    }

    @GetMapping("/viewCourseFeedback/{courseId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @ResponseBody
    public List<Feedback> getFeedbackByCourse(@AuthenticationPrincipal UserDetails user, @PathVariable String courseId) {
        List<Feedback> feedbackList = feedbackService.viewFeedbackByCourse(courseId);
        return feedbackList;
    }

    @GetMapping("/viewCourseFeedbackByInstructor/{instructorId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR')")
    @ResponseBody
    public List<Feedback> getFeedbackByInstructor(@AuthenticationPrincipal UserDetails user, @PathVariable String instructorId) {
        List<Feedback> feedbackList = feedbackService.viewFeedbackByInstructor(instructorId);
        return feedbackList;
    }

}