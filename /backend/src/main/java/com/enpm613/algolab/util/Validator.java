package com.enpm613.algolab.util;

import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@NoArgsConstructor
@Component
public class Validator {
    @Autowired
    UserRepository userRepository;

    public boolean validateUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isEmpty();
    }

    public boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean validatePassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }
}
