package com.enpm613.algolab;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.enpm613.algolab.entity.Announcement;
import com.enpm613.algolab.repository.AnnouncementRepository;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.service.AnnouncementService;
import com.enpm613.algolab.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementServiceTest {

    private AnnouncementService announcementService;
    @Mock
    private AnnouncementRepository announcementRepository;
    @Mock
    private S3Service s3Service;
    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        s3Service = mock(S3Service.class);
        announcementRepository = mock(AnnouncementRepository.class);
        mongoTemplate = mock(MongoTemplate.class);

        announcementService = new AnnouncementService(s3Service, mongoTemplate,announcementRepository);
    }

    @Test
    void testCreateAnnouncement() throws IOException {
        // Mock data
        Announcement newAnnouncement = new Announcement(null, "course123", "Test Announcement", "Announcement Content");
        User instructor = mock(User.class);

        // Mock repository save method
        when(announcementRepository.save(any(Announcement.class))).thenReturn(new Announcement("announcement123", "course123", "Test Announcement", "Announcement Content"));

        // Call the service method
        Announcement createdAnnouncement = announcementService.createAnnouncement(newAnnouncement, instructor);

        // Assertions
        assertNotNull(createdAnnouncement);
        assertEquals("announcement123", createdAnnouncement.getId());
        assertEquals("course123", createdAnnouncement.getCourseId());
        assertEquals("Test Announcement", createdAnnouncement.getTitle());
        assertEquals("Announcement Content", createdAnnouncement.getContent());
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }

    @Test
    void testGetAnnouncementsbyCourse() {
        // Mock data
        String courseId = "course123";
        List<Announcement> mockAnnouncements = new ArrayList<>();
        mockAnnouncements.add(new Announcement("announcement1", courseId, "Announcement 1", "Content 1"));
        mockAnnouncements.add(new Announcement("announcement2", courseId, "Announcement 2", "Content 2"));

        // Mock repository findByCourse method
        when(announcementRepository.findByCourse(courseId)).thenReturn(mockAnnouncements);

        // Call the service method
        List<Announcement> announcements = announcementService.getAnnouncementsbyCourse(courseId);

        // Assertions
        assertNotNull(announcements);
        assertEquals(2, announcements.size());
        assertEquals("announcement1", announcements.get(0).getId());
        assertEquals("course123", announcements.get(0).getCourseId());
        assertEquals("Announcement 1", announcements.get(0).getTitle());
        assertEquals("Content 1", announcements.get(0).getContent());
        // Add similar assertions for the second announcement
    }

    @Test
    void testDeleteAnnouncement() throws IOException {
        // Mock data
        String announcementId = "announcement123";

        // Call the service method
        announcementService.deleteAnnouncement(announcementId);

        // Verify that the deleteById method was called
        verify(announcementRepository, times(1)).deleteById(announcementId);
    }
}

