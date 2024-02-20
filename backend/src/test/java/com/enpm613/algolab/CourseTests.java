package com.enpm613.algolab;

import com.enpm613.algolab.controller.CourseController;
import com.enpm613.algolab.entity.Course;
import com.enpm613.algolab.entity.CourseDTO;
import com.enpm613.algolab.entity.Role;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.CourseRepository;
import com.enpm613.algolab.service.CourseService;
import com.enpm613.algolab.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.enpm613.algolab.entity.Course.Difficulty.EASY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CourseTests {

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private AuthenticationPrincipal authenticationPrincipal;

    @Mock
    private UserDetails userDetails;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void testCreateCourse() throws IOException {
        User mockUser = new User(null,"testUserCourse", "lastnamw", "test@example.com", "testUserCourse", "testUserCourse@1234", Role.INSTRUCTOR, "Bio");
        Mockito.when(userService.getUserByUsername("testUserCourse")).thenReturn(mockUser);

        // Mock file input stream and multipart file
        FileInputStream mockFileInputStream = Mockito.mock(FileInputStream.class);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.jpg", "multipart/form-data", mockFileInputStream);

        // Prepare course data
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("testCourse");
        courseDTO.setDifficulty(EASY);
        courseDTO.setDescription("Description");
        courseDTO.setImage(mockMultipartFile);

        // Mock courseRepository dependency
        Course mockSavedCourse = new Course("1","testCourse",EASY, "Description", mockUser);
        Mockito.when(courseService.createCourse(courseDTO,mockUser)).thenReturn(mockSavedCourse);

        // Create course using the mocked dependencies
        Course newCourse = courseService.createCourse(courseDTO, mockUser);
        Mockito.when(courseService.getCourseById(mockSavedCourse.getId())).thenReturn(mockSavedCourse);
        // Verify the saved course
        Course savedCourse = courseService.getCourseById(mockSavedCourse.getId());
        assertEquals(savedCourse, newCourse);

    }

//    @Test
//    public void testDeleteCourse() throws IOException {
//        User instructor = userService.getUserByUsername("barath98");
//        FileInputStream fileInputStream = new FileInputStream("src/main/resources/image.jpg");
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.jpg", "multipart/form-data", fileInputStream);
//        CourseDTO course = new CourseDTO();
//        course.setTitle("testCourse1");
//        course.setDifficulty(EASY);
//        course.setDescription("Description");
//        course.setImage(mockMultipartFile);
//        Course newCourse = courseService.createCourse(course,instructor);
//
//        courseService.deleteCourse(newCourse.getId());
//        assertEquals(null, courseRepository.findByCourseId(newCourse.getId()));
//
//
//    }

    @Test
    public void testGetAllCourses() throws IOException {
        User instructor = userService.getUserByUsername("barath98");
        List<Course> instructorCourses = courseService.getUserCourse(instructor);
        for(Course course:instructorCourses){
            boolean valid = courseService.checkCourse(instructor,course.getId());
            assertTrue(valid);
            assertEquals(instructor,course.getInstructor());
            assertEquals(course, courseService.getCourseById(course.getId()));
        }
        List<Course> serviceCourses = courseService.getAllCourses();


    }

    @Test
    public void testGetAllCoursesAPI() {
        List<Course> courses = new ArrayList<>();
        when(courseService.getAllCourses()).thenReturn(courses);

        ResponseEntity<Object> response = courseController.getAllCourses();
        System.out.println(response);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.toString());
        assertEquals("200 OK", response.getStatusCode().toString());
    }

    @Test
    public void testCheckCourse() throws IOException {
        User user = new User();
        User instructor = userService.getUserByUsername("barath98");
        //when(userService.getUserByUsername("barath98").thenReturn(user);
        when(courseService.checkCourse(instructor, "courseId")).thenReturn(true);

        ResponseEntity<Object> response = courseController.checkCourse(userDetails, "courseId");

        assertEquals("200 OK", response.getStatusCode().toString());
    }

    @Test
    public void testGetUserCourses() {
        User user = new User();
        User instructor = userService.getUserByUsername("barath98");
        List<Course> courses = new ArrayList<>();
        //when(userService.getUserByUsername(any())).thenReturn(user);
        when(courseService.getUserCourse(user)).thenReturn(courses);

        ResponseEntity<Object> response = courseController.getUserCourses(userDetails);

        assertEquals("200 OK", response.getStatusCode().toString());
        assertEquals(courses, response.getBody());
    }

//    @Test
//    public void testDeleteCourseAPI() throws IOException {
//        User instructor = userService.getUserByUsername("barath98");
//        FileInputStream fileInputStream = new FileInputStream("src/main/resources/image.jpg");
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.jpg", "multipart/form-data", fileInputStream);
//        CourseDTO course = new CourseDTO();
//        course.setTitle("testCourse1");
//        course.setDifficulty(EASY);
//        course.setDescription("Description");
//        course.setImage(mockMultipartFile);
//        Course newCourse = courseService.createCourse(course,instructor);
//        courseService.deleteCourse(newCourse.getId());
//
//        ResponseEntity<Object> response = courseController.deleteCourse(userDetails, "courseId");
//
//        assertEquals(200, response.getStatusCode().toString());
//    }


}
