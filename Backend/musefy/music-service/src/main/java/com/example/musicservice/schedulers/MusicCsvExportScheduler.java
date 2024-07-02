package com.example.musicservice.schedulers;

import com.example.musicservice.entities.Song;
import com.example.musicservice.repositories.SongRepository;
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
public class MusicCsvExportScheduler {

    private final SongRepository songRepository;
    private final Logger logger = Logger.getLogger(MusicCsvExportScheduler.class.getName());

    public MusicCsvExportScheduler(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void exportMusicToCsv() throws IOException {
        List<Song> songList = songRepository.findAll();
        // Get the project's root folder dynamically
        String rootFolder = System.getProperty("user.dir");

        // Construct the file path relative to the root folder using forward slashes
        String filePath = Paths.get(rootFolder, "..", "recommendation-service", "received_data", "songs.csv")
                .normalize()
                .toAbsolutePath()
                .toString()
                .replace("\\", "/");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            csvWriter.writeNext(new String[]{"song_id", "title", "genre", "duration", "tempo", "key"});
            logger.info("Writing songs data to CSV file.");
            // Write data rows
            for (Song song : songList) {
                csvWriter.writeNext(new String[]{song.getSongId(), song.getTitle(), song.getGenre(), String.valueOf(song.getDuration()), String.valueOf(song.getTempo()), song.getKey()});
            }
            logger.info("Songs data written to CSV file successfully.");
        }
    }
}

