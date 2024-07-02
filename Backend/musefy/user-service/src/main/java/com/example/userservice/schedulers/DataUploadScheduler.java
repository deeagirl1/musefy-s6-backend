package com.example.userservice.schedulers;

import com.example.userservice.entities.User;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DataUploadScheduler {

    private final UserRepository userRepository;
    private final Storage storage;
    private final ProducerService producerService;
    private final Logger logger = Logger.getLogger(DataUploadScheduler.class.getName());

    public DataUploadScheduler(UserRepository userRepository, Storage storage, ProducerService producerService) {
        this.userRepository = userRepository;
        this.producerService = producerService;
        this.storage = storage;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void uploadDataToGCPScheduled() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String userId = user.getId();
            producerService.sendExportUserDataMessage(userId);
            uploadDataToGCP(userId);
        }

    }

    private void uploadDataToGCP(String userId) {
        User user = userRepository.getUserById(userId);
        if (user!=null) {
            String folderName = "user_" + userId; // Update folder name without slashes

            try {
                // Create the CSV content
                String csvContent = "User ID,Name,Email,First Name,Last Name,Favorite Songs\n" +
                        String.format("%s,%s,%s,%s,%s,%s\n",
                                user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), String.join(",", user.getFavouriteSongs()));

                // Upload the CSV content to the folder
                String fileName = userId + ".csv";
                String filePath = folderName + "/" + fileName;
                BlobId blobId = BlobId.of("musefy-export-bucket", filePath);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/csv").build();
                storage.create(blobInfo, csvContent.getBytes());

                logger.info("User data uploaded to GCP successfully");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error uploading user data to GCP", e);
            }
        } else {
            logger.log(Level.SEVERE, "User not found with ID: {0}", userId);
        }
    }
}

