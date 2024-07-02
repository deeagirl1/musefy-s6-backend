package com.example.musicservice.repositories;

import com.example.musicservice.entities.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    Song getById(String id);
    Page<Song> findAllByTitleContainingIgnoreCase(String query, Pageable pageable);
    Page<Song> findAll(Pageable pageable);

}
