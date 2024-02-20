package com.enpm613.algolab.repository;

import com.enpm613.algolab.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{username: ?0}")
    Optional<User> findByUsername(String username);
}
