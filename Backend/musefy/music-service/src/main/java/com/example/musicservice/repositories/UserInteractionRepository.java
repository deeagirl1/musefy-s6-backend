package com.example.musicservice.repositories;

import com.example.musicservice.entities.UserSongInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserSongInteraction, String> {

    UserSongInteraction findByUserIdAndSongId(String userId, String songId);

    @Query("SELECT NEW map(u.userId AS userId, u.songId AS songId, u.playCount AS playCount) FROM UserSongInteraction u WHERE u.userId = :userId")
    Map<String, Object> findInteractionDataByUserId(@Param("userId") String userId);

    @Query("SELECT NEW map(u.userId AS userId, u.songId AS songId, u.playCount AS playCount) FROM UserSongInteraction u")
    List<Map<String, Object>> findAllInteractionData();

    void deleteByUserId(String userId);

    @Query("SELECT u FROM UserSongInteraction u WHERE u.userId = :userId")
    List<UserSongInteraction> getUserInteractionsByUserId(@Param("userId") String userId);
}
