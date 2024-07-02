package com.example.musicservice.controllers;

import com.example.musicservice.entities.Album;
import com.example.musicservice.services.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping()
    public List<Album> getPlaylists() {
        return albumService.getAlbums();
    }

    @GetMapping("/{id}")
    public Optional<Album> getPlaylistById(@PathVariable String id) {
        return albumService.getAlbumById(id);
    }

    @PostMapping("/create")
    public Album createAlbum(@RequestBody Album album) {
        return albumService.createAlbum(album);
    }

    @PostMapping("/addSong")
    public Album addSongsToAlbum(@RequestParam String albumId, @RequestParam String songId) {

        return albumService.addSongToAlbum(albumId,songId);
    }

}
