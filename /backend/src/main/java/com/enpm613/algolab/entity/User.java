package com.enpm613.algolab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Role role;
    private String bio;
}
