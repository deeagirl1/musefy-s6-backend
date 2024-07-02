package com.example.musicservice.services;

import com.example.musicservice.entities.Album;
import com.example.musicservice.entities.Song;
import com.example.musicservice.repositories.AlbumRepository;
import com.example.musicservice.repositories.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, SongRepository songRepository) {
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public List<Album> getAlbums() {
        return albumRepository.findAll();
    }

    public Optional<Album> getAlbumById(String id) {
        return albumRepository.findById(id);
    }

    public Album createAlbum (Album album){
        return albumRepository.save(album);
    }

    public Album addSongToAlbum(String albumId, String songId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new EntityNotFoundException("Song not found"));

        song.setAlbum(album);
        album.getSongs().add(song);

        albumRepository.save(album);
        songRepository.save(song);
        return album;
    }



}
