package com.enpm613.algolab.service;

import com.enpm613.algolab.entity.Issue;
import com.enpm613.algolab.entity.User;
import com.enpm613.algolab.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    public Issue createIssue(Issue issue, User curUser) {
        issue.setUser(curUser);
        return issueRepository.save(issue);
    }

    public Issue getIssueById(String id) {
        Optional<Issue> optional = issueRepository.findById(id);
        Issue issue = null;
        if (optional.isPresent()) {
            issue = optional.get();
        } else {
            throw new RuntimeException(" Issue not found ");
        }
        return issue;

    }

    public void deleteIssue(String id) {
        issueRepository.deleteById(id);
    }
}
