package com.example.musicservice.services;

import com.example.musicservice.entities.Song;
import com.example.musicservice.repositories.SongRepository;
import com.google.cloud.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class SongService {
    private final SongRepository songRepository;

    private static final String BUCKET_NAME_SONGS = "musefys6-songs-bucket";
    private final Storage storage;

    private final Logger logger = LoggerFactory.getLogger(SongService.class);

    @Autowired
    public SongService(SongRepository songRepository, Storage storage) {
        this.songRepository = songRepository;
        this.storage = storage;
    }
    public Page<Song> getAllSongs(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    public Optional<Song> getSongById(String id) {
        return songRepository.findById(id);
    }
    public void createSong(Song song, MultipartFile mp3File, MultipartFile imageFile) throws IOException {
        String songFileId = UUID.randomUUID().toString();
        String imageFileId = UUID.randomUUID().toString();
        String songUrl = String.format("https://storage.googleapis.com/%s/songs/%s", BUCKET_NAME_SONGS, songFileId);
        String imageUrl = String.format("https://storage.googleapis.com/%s/images/%s", BUCKET_NAME_SONGS, imageFileId);

        BlobInfo songBlobInfo = BlobInfo.newBuilder(BlobId.of(BUCKET_NAME_SONGS, "songs/" + songFileId))
                .setContentType("audio/mpeg")
                .build();
        storage.create(songBlobInfo, mp3File.getBytes());

        BlobInfo imageBlobInfo = BlobInfo.newBuilder(BlobId.of(BUCKET_NAME_SONGS, "images/" + imageFileId))
                .setContentType(imageFile.getContentType())
                .build();
        storage.create(imageBlobInfo, imageFile.getBytes());

        song.setMp3GCPLink(songUrl);
        song.setImageLink(imageUrl);
        songRepository.save(song);
    }

    public Page<Song> searchSongs(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        return songRepository.findAllByTitleContainingIgnoreCase(query, pageable);
    }

    public void deleteSong(String songId) {
        // Fetch the Song entity, assuming it contains the file names
        Optional<Song> songOptional = songRepository.findById(songId);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();

            // Get file names
            String imageFileName = song.getImageLink();
            String mp3FileName = song.getMp3GCPLink();

            // Delete the files from the storage
            storage.delete(BUCKET_NAME_SONGS, imageFileName);
            storage.delete(BUCKET_NAME_SONGS, mp3FileName);

            // Delete the song from the repository
            songRepository.deleteById(songId);
        } else {
            // Handle the case when no song is found with the provided id
            logger.error("Song with id {} not found", songId);
        }
    }
}

