package com.enpm613.algolab.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enpm613.algolab.entity.Announcement;
import com.enpm613.algolab.entity.Course;
import com.enpm613.algolab.entity.CourseDTO;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.service.AnnouncementService;
import com.enpm613.algolab.service.CourseService;
import com.enpm613.algolab.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/announcement")
@AllArgsConstructor
@CrossOrigin("*")
public class AnnouncementController {
    @Autowired
    AnnouncementService announcementService;

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

    @GetMapping("/allAnnouncements/{courseId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT','ADMIN')")
    @ResponseBody
    public ResponseEntity<Object> getAllAnnouncementsbyCourse(@AuthenticationPrincipal UserDetails user,  @PathVariable String courseId) {
        //It's a sample request to view announcement
        logger.debug("Inside getAnnouncements : " );
        return ResponseEntity.ok(announcementService.getAnnouncementsbyCourse(courseId));
    }

    @PostMapping("/createAnnouncement")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @ResponseBody
    public ResponseEntity<Object> createAnnouncement(@AuthenticationPrincipal UserDetails user,  @RequestBody Announcement newAnnouncement) throws IOException {
        //It's a sample request to create announcement
        logger.debug("Inside createAnnouncement : " );
        String curUsername = user.getUsername();
        User curUser = userService.getUserByUsername(curUsername);

        return ResponseEntity.ok(announcementService.createAnnouncement(newAnnouncement, curUser));
    }

    @DeleteMapping("/deleteAnnouncement/{announcementId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @ResponseBody
    public ResponseEntity<Object> deleteAnnouncement(@AuthenticationPrincipal UserDetails user,  @PathVariable String announcementId) throws IOException {
        //It's a sample request to create announcement
        logger.debug("Inside deleteAnnouncement : " );
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok("true");
    }
}
