package com.example.musicservice.controllers;

import com.example.musicservice.entities.Song;
import com.example.musicservice.entities.UserSongInteraction;
import com.example.musicservice.services.SongService;
import com.example.musicservice.services.UserInteractionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private UserInteractionService userInteractionService;

    @GetMapping("/{id}")
    public Optional<Song> getSongById(@PathVariable String id) {
        return songService.getSongById(id);
    }

    @GetMapping()
    public Page<Song> getAllSongs(Pageable pageable) {
        return songService.getAllSongs(pageable);
    }

    @PostMapping ("/create")
    public ResponseEntity<Song> sendData(@RequestParam("songFile") MultipartFile songFile, @RequestParam("imageFile") MultipartFile imageFile , String jsonFileVo) {
        try {
            if (songFile == null || imageFile == null || jsonFileVo == null) {
                return ResponseEntity.notFound().build();
            } else {
                Song song = new ObjectMapper().readValue(jsonFileVo, Song.class);
                songService.createSong(song,songFile,imageFile);
                return ResponseEntity.ok().body(song);
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

    }

    @GetMapping("/search")
    public Page<Song> searchSongs(@RequestParam String query, @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return songService.searchSongs(query, page, size);
    }

    @GetMapping("/interactions/{userId}")
    public List<UserSongInteraction> getInteractions(@PathVariable String userId) {
        return userInteractionService.getAllUserSongInteractionsByUserId(userId);
    }


}

