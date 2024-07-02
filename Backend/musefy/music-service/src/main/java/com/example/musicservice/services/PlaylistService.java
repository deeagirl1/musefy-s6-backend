package com.example.musicservice.services;

import com.example.musicservice.entities.Playlist;
import com.example.musicservice.entities.Song;
import com.example.musicservice.repositories.PlaylistRepository;
import com.example.musicservice.repositories.SongRepository;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final Storage storage;
    private static final String BUCKET_NAME_SONGS = "musefys6-songs-bucket";


    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository, Storage storage) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.storage = storage;
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Optional<Playlist> getPlaylistById(String id) {
        return playlistRepository.findById(id);
    }

    public Playlist createPlaylist(Playlist playlist, MultipartFile playlistPicture, String userId) throws IOException {
            // Generate unique file ID for playlist picture
            String playlistPictureFileId = UUID.randomUUID().toString();
            String playlistPictureUrl = String.format("https://storage.googleapis.com/%s/playlist/pictures/%s", BUCKET_NAME_SONGS, playlistPictureFileId);

            // Create BlobInfo for playlist picture
            BlobInfo playlistPictureBlobInfo = BlobInfo.newBuilder(BlobId.of(BUCKET_NAME_SONGS, "playlist-pictures/" + playlistPictureFileId))
                    .setContentType(playlistPicture.getContentType())
                    .build();

            // Upload playlist picture to Cloud Storage
            storage.create(playlistPictureBlobInfo, playlistPicture.getBytes());

            // Set playlist picture URL
            playlist.setImage(playlistPictureUrl);
            playlist.setCreatedBy(userId);

            // Save the playlist to the repository
            playlistRepository.save(playlist);

        return playlist;
    }

    public Playlist addSongToPlaylist(String playlistId, String songId) {
        Playlist playlist = this.getPlaylistById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new EntityNotFoundException("Song not found"));

        playlist.getSongs().add(song);
        song.getPlaylists().add(playlist);

        playlist = playlistRepository.save(playlist);
        songRepository.save(song);

        return playlist;
    }

    public Playlist removeSongFromPlaylist(String playlistId, String songId) {
        Playlist playlist = this.getPlaylistById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new EntityNotFoundException("Song not found"));

        playlist.getSongs().remove(song);
        song.getPlaylists().remove(playlist);

        playlist = playlistRepository.save(playlist);
        songRepository.save(song);

        return playlist;
    }

    public Playlist updatePlaylist(String playlistId, Playlist playlist) {
        Playlist playlistToUpdate = this.getPlaylistById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found"));

        playlistToUpdate.setDescription(playlist.getDescription());
        playlistToUpdate.setDescription(playlist.getDescription());
        playlistToUpdate.setImage(playlist.getImage());
        playlistToUpdate.setSongs(playlist.getSongs());

        return playlistRepository.save(playlistToUpdate);
    }

    public void deletePlaylist(String playlistId) {
        Playlist playlist = this.getPlaylistById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found"));

        playlistRepository.delete(playlist);
    }

    public List<Song> getSongsFromPlaylist(String playlistId) {
        Playlist playlist = this.getPlaylistById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found"));

        return playlist.getSongs();
    }

    public List<Playlist> getPlaylistsByUserId(String userId) {
        return playlistRepository.getPlaylistsByCreatedBy(userId);
    }

    public void deleteByCreatedBy(String userId) {
        playlistRepository.deleteByCreatedBy(userId);
    }

}
