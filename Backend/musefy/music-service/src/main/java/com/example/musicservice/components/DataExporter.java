package com.example.musicservice.components;

import com.example.musicservice.entities.Playlist;
import com.example.musicservice.entities.UserSongInteraction;
import com.example.musicservice.services.PlaylistService;
import com.example.musicservice.services.UserInteractionService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class DataExporter {
    private final UserInteractionService userInteractionService;
    private final PlaylistService playlistService;
    private final Storage storage;

    public DataExporter(UserInteractionService userInteractionService, PlaylistService playlistService, Storage storage) {
        this.userInteractionService = userInteractionService;
        this.playlistService = playlistService;
        this.storage = storage;
    }

    public void uploadUserDataToGCP(String userId) {
        List<UserSongInteraction> interactions = userInteractionService.getAllUserSongInteractionsByUserId(userId);
        List<Playlist> playlists = playlistService.getPlaylistsByUserId(userId);

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("user_data.csv"))) {
            // Write header for user interactions
            csvWriter.writeNext(new String[]{"User ID", "Song ID", "Play Count"});

            for (UserSongInteraction interaction : interactions) {
                csvWriter.writeNext(new String[]{interaction.getUserId(), interaction.getSongId(), String.valueOf(interaction.getPlayCount())});
            }

            // Write header for playlists
            csvWriter.writeNext(new String[]{"User ID", "Playlist ID", "Playlist Title"});

            for (Playlist playlist : playlists) {
                csvWriter.writeNext(new String[]{playlist.getCreatedBy(), playlist.getId(), playlist.getTitle()});
            }

            csvWriter.flush();

            // Upload CSV file to Cloud Storage
            BlobId blobId = BlobId.of("musefy-export-bucket", "user_" + userId + "/user_data_" + userId + ".csv");
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, Files.readAllBytes(Paths.get("user_data.csv")));

            // Log a success message or perform any additional actions

        } catch (IOException e) {
            // Handle the exception or log an error message
            e.printStackTrace();
        }
    }

}

