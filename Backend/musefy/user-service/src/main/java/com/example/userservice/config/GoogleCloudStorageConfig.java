package com.example.userservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@Configuration
public class GoogleCloudStorageConfig {

    private GoogleCredentials credentials;
    private final Logger logger = Logger.getLogger(GoogleCloudStorageConfig.class.getName());
    @PostConstruct
    public void init() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("service-account.json");
            assert inputStream != null;
            credentials = GoogleCredentials.fromStream(inputStream);
        } catch (IOException e) {
            logger.severe("Error occurred while reading credentials from file. Error message: " + e.getMessage());
        }
    }

    @Bean
    public GoogleCredentials googleCredentials() {
        return credentials;
    }

    @Bean
    public Storage storage() {
        StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).build();
        return options.getService();
    }
}

