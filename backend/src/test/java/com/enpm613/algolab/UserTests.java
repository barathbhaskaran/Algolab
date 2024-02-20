package com.enpm613.algolab;

import com.enpm613.algolab.controller.UserController;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.UserRepository;
import com.enpm613.algolab.service.UserService;
import com.enpm613.algolab.util.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.enpm613.algolab.entity.Role.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private UserService userService;

    @Test
    public void testRegisterUser() {
        //get student user details
        User user = new User();
        user.setFirstName("testStudent");
        user.setLastName("test");
        user.setEmail("test@email.com");
        user.setUsername("testStudent");
        user.setPassword("testStudent@1234");
        user.setRole(STUDENT);
        user.setBio("test");
        User savedUser = new User();
        if (userService.isValidUsername(user.getUsername())) {
            savedUser = userService.registerUser(user);
        } else{
            savedUser = userService.getUserByUsername(user.getUsername());
        }
        User checkUser = userService.getUserByUsername(user.getUsername());
        assertEquals(savedUser, checkUser);
        userService.deleteUser(user.getId());
    }

        @Test
        public void testRegisterInstructor() {
            //get student user details
            User user = new User();
            user.setFirstName("testInstructor");
            user.setLastName("test");
            user.setEmail("testInstructor@email.com");
            user.setUsername("testInstructor");
            user.setPassword("testInstructor@1234");
            user.setRole(INSTRUCTOR);
            user.setBio("testInstructor");
            User savedUser = new User();
            if (userService.isValidUsername(user.getUsername())) {
                savedUser = userService.registerUser(user);
            }else{
                savedUser = userService.getUserByUsername(user.getUsername());
            }
            User checkUser = userService.getUserByUsername(user.getUsername());
            assertEquals(savedUser, checkUser);
            userService.deleteUser(user.getId());
        }

    @Test
    public void testRegisterAdmin() {
        //get student user details
        User user = new User();
        user.setFirstName("testAdmin");
        user.setLastName("test");
        user.setEmail("testAdmin@email.com");
        user.setUsername("testAdmin");
        user.setPassword("testAdmin@1234");
        user.setRole(ADMIN);
        user.setBio("testAdmin");
        User savedUser = new User();
        if (userService.isValidUsername(user.getUsername())) {
            savedUser = userService.registerUser(user);
        }else{
            savedUser = userService.getUserByUsername(user.getUsername());
        }
        User checkUser = userService.getUserByUsername(user.getUsername());
        assertEquals(savedUser, checkUser);
        userService.deleteUser(user.getId());
    }

    @Test
    public void testUpdateUser() {
        //get student user details
        User user = new User();
        user.setFirstName("testStudent1");
        user.setLastName("test");
        user.setEmail("test@email.com");
        user.setUsername("testStudent1");
        user.setPassword("testStudent@1234");
        user.setRole(STUDENT);
        user.setBio("test");
        if (userService.isValidUsername(user.getUsername())) {
            userService.registerUser(user);
            User updatedUser = new User(null,"username1", "test", "test@email.com", "testStudent1", "SwathiSelvakumaran@17", STUDENT, "Bio");


            userService.updateUser(updatedUser);
            User savedUser = userService.getUserByUsername("testStudent1");
            assertEquals(updatedUser, savedUser);

        } else{

            User updatedUser = new User(null,"username", "test", "test@email.com", "testStudent1", "SwathiSelvakumaran@17", STUDENT, "Bio");
            User savedUser = userService.getUserByUsername("testStudent1");
            userService.updateUser(updatedUser);
            assertEquals(updatedUser, savedUser);
        }

    }
    @Test
    public void testDeleteUser() {

        User user = new User();
        user.setFirstName("testInstructor");
        user.setLastName("test");
        user.setEmail("testInstructor@email.com");
        user.setUsername("testInstructor");
        user.setPassword("testInstructor@1234");
        user.setRole(INSTRUCTOR);
        user.setBio("testInstructor");
        User savedUser = new User();
        if (userService.isValidUsername(user.getUsername())) {
            savedUser = userService.registerUser(user);
        }else{
            savedUser = userService.getUserByUsername(user.getUsername());
        }


        userService.deleteUser(savedUser.getId());
    }

    @Test
    public void testGetUser(){
        List<User> user = userService.getAllUsers();
        for(User curUser: user){
            User testUserWithId = userService.getUser(curUser.getId());
            User testUserWithUsername = userService.getUser(curUser.getId());
            assertEquals(testUserWithId,testUserWithUsername);
            assertEquals(testUserWithId,curUser);
            assertEquals(testUserWithUsername,curUser);
        }
    }


}
