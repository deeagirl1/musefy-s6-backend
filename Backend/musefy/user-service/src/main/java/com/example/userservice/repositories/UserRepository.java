package com.example.userservice.repositories;

import com.example.userservice.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User getUserById(String id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteUserByUsername(String username);

    void deleteUserById(String userId);

    User findUserByUsername(String username);

    boolean existsByIdAndFavouriteSongsContaining(String userId, String songId);



}
