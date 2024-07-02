package com.example.userservice.schedulers;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.services.UserService;
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
public class UserCsvExportScheduler {

    private final UserService userService;
    private final Logger logger = Logger.getLogger(UserCsvExportScheduler.class.getName());

    public UserCsvExportScheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void exportUserData() throws IOException {
        List<UserDTO> userList = userService.getAllUsers();
        // Get the project's root folder dynamically
        String rootFolder = System.getProperty("user.dir");
        // Construct the file path relative to the root folder using forward slashes
        String filePath = Paths.get(rootFolder, "..", "recommendation-service", "received_data", "users.csv")
                .normalize()
                .toAbsolutePath()
                .toString()
                .replace("\\", "/");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            csvWriter.writeNext(new String[]{"user_id", "username"});
            logger.info("Writing data to CSV file.");
            // Write data rows
            for (UserDTO user : userList) {
                csvWriter.writeNext(new String[]{user.getId(), user.getUsername()});
            }
            logger.info("Data written to CSV file successfully.");
        }
    }
}



