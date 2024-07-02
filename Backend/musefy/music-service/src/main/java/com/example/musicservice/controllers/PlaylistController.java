package com.example.musicservice.controllers;

import com.example.musicservice.entities.Playlist;
import com.example.musicservice.entities.Song;
import com.example.musicservice.services.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;


    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/")
    public List<Playlist> getPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/{id}")
    public Optional<Playlist> getPlaylistById(@PathVariable String id) {
        return playlistService.getPlaylistById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Playlist> createPlaylist(@RequestParam("imageFile") MultipartFile imageFile , String jsonFileVo) {
        try {
            if (imageFile == null || jsonFileVo == null) {
                return ResponseEntity.notFound().build();
            } else {
                Playlist playlist = new ObjectMapper().readValue(jsonFileVo, Playlist.class);
                Playlist createdPlaylist = playlistService.createPlaylist(playlist, imageFile, playlist.getCreatedBy());

                return ResponseEntity.ok().body(createdPlaylist);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    @PostMapping("/addSong")
    public Playlist addSong(@RequestParam String playlistId, @RequestParam String songId) {

        return playlistService.addSongToPlaylist(playlistId,songId);
    }

    @PostMapping("/removeSong")
    public Playlist removeSong(@RequestParam String playlistId, @RequestParam String songId) {

        return playlistService.removeSongFromPlaylist(playlistId,songId);
    }

    @PostMapping("/delete")
    public ResponseEntity<Playlist> deletePlaylist(@RequestParam String playlistId) {
        try {
            if (playlistId == null) {
                return ResponseEntity.notFound().build();
            } else {
                playlistService.deletePlaylist(playlistId);

                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    @GetMapping("{playlistId}/songs")
    public List<Song> getSongs(@PathVariable String playlistId) {
        return playlistService.getSongsFromPlaylist(playlistId);
    }

    @GetMapping("{userId}/playlists")
    public List<Playlist> getUserPlaylists(@PathVariable String userId) {
        return playlistService.getPlaylistsByUserId(userId);
    }

}