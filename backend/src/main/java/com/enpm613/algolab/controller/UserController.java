package com.enpm613.algolab.controller;

import com.enpm613.algolab.entity.CustomUserDetails;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.entity.AuthRequest;
import com.enpm613.algolab.entity.LoginResponse;
import com.enpm613.algolab.service.JWTService;
import com.enpm613.algolab.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @GetMapping("/userDetails")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT','ADMIN')")
    public ResponseEntity<Object> getUser(@AuthenticationPrincipal UserDetails user) {
        try {
            String curUsername = user.getUsername();
            return ResponseEntity.ok(userService.getUserByUsername(curUsername));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/userDetails")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT','ADMIN')")
    public ResponseEntity<Object> UpdateUser(@AuthenticationPrincipal UserDetails user, @RequestBody User updatedUser) {
        try {
            String curUsername = user.getUsername();
            User curUser = userService.getUserByUsername(curUsername);

//            System.out.println("IDS : "+curUser.getId()+" "+updatedUser.getId());
            if( !curUser.getId().equals(updatedUser.getId()) ){
                return ResponseEntity.badRequest().build();
            }
            userService.updateUser(updatedUser);
            UserDetails updatedUserDetails = new CustomUserDetails(updatedUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(updatedUserDetails, null, updatedUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return ResponseEntity.ok(jwtService.GenerateToken(updatedUser.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.registerUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") String userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.isValidUsername(username));
    }



    @PostMapping("/login")
    public LoginResponse AuthenticateAndGetToken(@RequestBody AuthRequest authRequestDTO){
        logger.debug("Inside login post method");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        String role = userService.getUserByUsername(authRequestDTO.getUsername()).getRole().toString();
        if(authentication.isAuthenticated()){
            return LoginResponse.builder()
                    .token(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .role(role)
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
        
    }


}
