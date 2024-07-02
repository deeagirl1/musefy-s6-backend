package com.example.musicservice.repositories;

import com.example.musicservice.entities.UserFavoriteSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteSongRepository extends JpaRepository<UserFavoriteSong, String> {
    void deleteByUserId(String userId);

    boolean existsByUserIdAndSongId(String userId, String songId);

    List<UserFavoriteSong> findByUserId(String userId);
}
