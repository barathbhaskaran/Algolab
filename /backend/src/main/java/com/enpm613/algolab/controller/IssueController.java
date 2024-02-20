package com.enpm613.algolab.controller;

import com.enpm613.algolab.entity.Issue;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.service.IssueService;
import com.enpm613.algolab.service.UserService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("api/v1")
@CrossOrigin("*")
public class IssueController {
    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/issues")
    public ResponseEntity<Object> getIssues() {
        return ResponseEntity.ok(issueService.getIssues());
    }

    @PostMapping("/reportIssue")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public ResponseEntity<Object> createIssue(@RequestBody Issue issue, @AuthenticationPrincipal UserDetails user) {
        String curUsername = user.getUsername();
        User curUser = userService.getUserByUsername(curUsername);
        issueService.createIssue(issue,curUser);
        return ResponseEntity.ok("true");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/issues/{id}")
    public ResponseEntity<Object> getIssue(@PathVariable String id) {
        return ResponseEntity.ok(issueService.getIssueById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Object> deleteIssue(@PathVariable String id) {
        issueService.deleteIssue(id);
        return ResponseEntity.ok("true");
    }

}
