package com.enpm613.algolab.service;

import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.UserRepository;
import com.enpm613.algolab.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    Validator validator;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public User registerUser(User user) {
        if (!validator.validateUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }
        if (!validator.validateEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }
        if (!validator.validatePassword(user.getPassword())) {
            throw new RuntimeException("Password is not strong enough.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateUser(User updatedUser) {
        Optional<User> newUser = userRepository.findByUsername(updatedUser.getUsername());

        // Check if the updated username is valid and not taken by another user
        if (newUser.isPresent() && !newUser.get().getId().equals(updatedUser.getId()) &&
                !validator.validateUsername(updatedUser.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }



        if( !updatedUser.getPassword().startsWith("$2a$") ){
            if (!validator.validatePassword(updatedUser.getPassword())) {
                throw new RuntimeException("Password is not strong enough.");
            }
            updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(updatedUser);
//        userRepositoryCustom.updateUser(updatedUser);

    }

    public void deleteUser(String userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        userRepository.delete(existingUser);
    }

    public boolean isValidUsername(String username) {
        return validator.validateUsername(username);
    }
}
