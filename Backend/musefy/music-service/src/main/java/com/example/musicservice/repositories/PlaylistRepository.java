package com.example.musicservice.repositories;

import com.example.musicservice.entities.Playlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
    @Modifying
    @Query(value = "INSERT INTO Playlist (name) VALUES (:name)", nativeQuery = true)
    @Transactional
    void createPlaylist(@Param("name") String name);

    void deleteByCreatedBy(String userId);

    List<Playlist> getPlaylistsByCreatedBy(String userId);



}

