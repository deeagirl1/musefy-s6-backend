package com.example.musicservice.IntegrationTests;

import com.example.musicservice.entities.Playlist;
import com.example.musicservice.entities.Song;
import com.example.musicservice.repositories.PlaylistRepository;
import com.example.musicservice.repositories.SongRepository;
import com.example.musicservice.services.PlaylistService;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaylistServiceTest {
    private static final String BUCKET_NAME_SONGS = "musefy-songs-bucket" ;
    @Mock
    private PlaylistRepository playlistRepository;
    @Mock
    private SongRepository songRepository;
    @Mock
    private Storage storage;

    private PlaylistService playlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlistService = new PlaylistService(playlistRepository, songRepository, storage);
    }

    @Test
    void getAllPlaylists_ReturnsAllPlaylists() {
        // Arrange
        List<Playlist> expectedPlaylists = Arrays.asList(
                new Playlist("1", "Playlist 1"),
                new Playlist("2", "Playlist 2"),
                new Playlist("3", "Playlist 3")
        );
        when(playlistRepository.findAll()).thenReturn(expectedPlaylists);

        // Act
        List<Playlist> playlists = playlistService.getAllPlaylists();

        // Assert
        assertEquals(expectedPlaylists.size(), playlists.size());
        assertEquals(expectedPlaylists, playlists);
    }

    @Test
    void getPlaylistById_ReturnsPlaylist_WhenFound() {
        // Arrange
        String playlistId = "1";
        Playlist expectedPlaylist = new Playlist(playlistId, "Playlist 1");
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(expectedPlaylist));

        // Act
        Optional<Playlist> playlist = playlistService.getPlaylistById(playlistId);

        // Assert
        assertTrue(playlist.isPresent());
        assertEquals(expectedPlaylist, playlist.get());
    }

    @Test
    void getPlaylistById_ReturnsEmptyOptional_WhenNotFound() {
        // Arrange
        String playlistId = "1";
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // Act
        Optional<Playlist> playlist = playlistService.getPlaylistById(playlistId);

        // Assert
        assertFalse(playlist.isPresent());
    }

    @Test
    void createPlaylist_CreatesPlaylist_WithPlaylistPicture() throws IOException {
        // Arrange
        Playlist playlist = new Playlist("1", "My Playlist");
        MultipartFile playlistPicture = new MockMultipartFile(
                "picture.jpg",
                "picture.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );
        String userId = "12345";

        doAnswer(invocation -> {
            BlobInfo blobInfo = invocation.getArgument(0);
            byte[] content = invocation.getArgument(1);

            // Process the blobInfo and content
            // For example, you can generate a unique file ID and construct the URL

            String fileId = UUID.randomUUID().toString();
            String fileUrl = String.format("https://storage.googleapis.com/%s/playlist/pictures/%s", BUCKET_NAME_SONGS, fileId);

            // Perform additional operations if needed

            // Set the playlist image URL
            playlist.setImage(fileUrl);

            // Return the URL
            return null;
        }).when(storage).create(any(BlobInfo.class), any(byte[].class));

        // Act
        Playlist createdPlaylist = playlistService.createPlaylist(playlist, playlistPicture, userId);

        // Assert
        assertEquals(playlist, createdPlaylist);
        assertEquals(userId, playlist.getCreatedBy());

        verify(playlistRepository).save(playlist);
    }


    @Test
    void addSongToPlaylist_AddsSongToPlaylist() {
        // Arrange
        String playlistId = "1";
        String songId = "10";
        Playlist playlist = new Playlist(playlistId, "My Playlist");
        Song song = new Song(songId, "Song 1");

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(playlistRepository.save(playlist)).thenReturn(playlist);
        when(songRepository.save(song)).thenReturn(song);

        // Act
        Playlist updatedPlaylist = playlistService.addSongToPlaylist(playlistId, songId);

        // Assert
        assertTrue(updatedPlaylist.getSongs().contains(song));
        assertTrue(song.getPlaylists().contains(playlist));

        verify(playlistRepository).save(playlist);
        verify(songRepository).save(song);
    }

    @Test
    void removeSongFromPlaylist_RemovesSongFromPlaylist() {
        // Arrange
        String playlistId = "1";
        String songId = "10";
        Playlist playlist = new Playlist(playlistId, "My Playlist");
        Song song = new Song(songId, "Song 1");

        playlist.getSongs().add(song);
        song.getPlaylists().add(playlist);

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(playlistRepository.save(playlist)).thenReturn(playlist);
        when(songRepository.save(song)).thenReturn(song);

        // Act
        Playlist updatedPlaylist = playlistService.removeSongFromPlaylist(playlistId, songId);

        // Assert
        assertFalse(updatedPlaylist.getSongs().contains(song));
        assertFalse(song.getPlaylists().contains(playlist));

        verify(playlistRepository).save(playlist);
        verify(songRepository).save(song);
    }

    @Test
    void updatePlaylist_UpdatesPlaylist() {
        // Arrange
        String playlistId = "1";
        Playlist existingPlaylist = new Playlist(playlistId, "Playlist 1");
        Playlist updatedPlaylist = new Playlist(playlistId, "Updated Playlist");

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(existingPlaylist));
        when(playlistRepository.save(existingPlaylist)).thenReturn(existingPlaylist);

        // Act
        Playlist result = playlistService.updatePlaylist(playlistId, updatedPlaylist);

        // Assert
        assertEquals(updatedPlaylist.getDescription(), result.getDescription());
        assertEquals(updatedPlaylist.getImage(), result.getImage());
        assertEquals(updatedPlaylist.getSongs(), result.getSongs());

        verify(playlistRepository).save(existingPlaylist);
    }

    @Test
    void deletePlaylist_DeletesPlaylist() {
        // Arrange
        String playlistId = "1";
        Playlist playlist = new Playlist(playlistId, "Playlist 1");

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        // Act
        playlistService.deletePlaylist(playlistId);

        // Assert
        verify(playlistRepository).delete(playlist);
    }
}
