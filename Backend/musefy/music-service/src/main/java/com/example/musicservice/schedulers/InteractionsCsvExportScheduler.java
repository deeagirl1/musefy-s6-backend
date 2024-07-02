package com.example.musicservice.schedulers;

import com.example.musicservice.entities.UserSongInteraction;
import com.example.musicservice.exceptions.CsvErrorException;
import com.example.musicservice.repositories.UserInteractionRepository;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Service
@Slf4j
public class InteractionsCsvExportScheduler {

    private final UserInteractionRepository userInteractionRepository;
    private final Logger logger = Logger.getLogger(InteractionsCsvExportScheduler.class.getName());

    public InteractionsCsvExportScheduler(UserInteractionRepository userInteractionRepository) {
        this.userInteractionRepository = userInteractionRepository;
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void exportMusicToCsv() throws IOException {
        List<UserSongInteraction> songList = userInteractionRepository.findAll();
        // Get the project's root folder dynamically
        String rootFolder = System.getProperty("user.dir");

        // Construct the file path relative to the root folder using forward slashes
        String filePath = Paths.get(rootFolder, "..", "recommendation-service", "received_data", "interactions.csv")
                .normalize()
                .toAbsolutePath()
                .toString()
                .replace("\\", "/");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            csvWriter.writeNext(new String[]{"user_id", "song_id", "listen_count"});
            logger.info("Writing interactions data to CSV file.");
            // Write data rows
            for (UserSongInteraction interaction : songList) {
                csvWriter.writeNext(new String[]{interaction.getUserId(), interaction.getSongId(), String.valueOf(interaction.getPlayCount())});
            }
            logger.info("Interactions data written to CSV file successfully.");
        }
        catch (Exception ex)
        {
            throw new CsvErrorException(ex.getMessage());
        }
    }
}

