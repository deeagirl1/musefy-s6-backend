package com.example.musicservice.repositories;


import com.example.musicservice.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AlbumRepository extends JpaRepository<Album, String>  {
}