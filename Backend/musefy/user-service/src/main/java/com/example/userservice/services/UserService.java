package com.example.userservice.services;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserFavoriteSongDTO;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.InputOutputException;
import com.example.userservice.repositories.UserRepository;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final Storage storage;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    public UserService(UserRepository userRepository, Storage storage) {
        this.userRepository = userRepository;
        this.storage = storage;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTO::from)
                .toList();
    }


    public UserDTO getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserDTO::from).orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));
    }


    public void addSongToFavouriteList(UserFavoriteSongDTO userFavoriteSongDTO) {
        String userId = userFavoriteSongDTO.getUserId();
        String songId = userFavoriteSongDTO.getSongId();
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> {
            u.getFavouriteSongs().add(songId);
            userRepository.save(u);
        });
        logger.log(Level.SEVERE, "User not found with ID: {0} ", userId);
    }

    public void saveRecommendation(String userId,List<String> recommendedSongs) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> {
            u.setRecommendedSongs(recommendedSongs);
            userRepository.save(u);
        });
        logger.log(Level.SEVERE, "User not found with ID: {0} ", userId);
    }

    public List<String> getUserFavoriteSongs(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        return user.getFavouriteSongs();
    }

//    public String downloadUserData(String userId) {
//        String objectName = "user_" + userId + "/" + userId + ".csv";
//        BlobId blobId = BlobId.of("musefy-export-bucket", objectName);
//        Blob blob = storage.get(blobId);
//
//        if (blob != null) {
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//            long duration = 3600; // URL expiration time in seconds
//
//            URL signedUrl = storage.signUrl(blobInfo, duration, TimeUnit.SECONDS, Storage.SignUrlOption.httpMethod(HttpMethod.GET));
//            return signedUrl.toString();
//        } else {
//            throw new IllegalArgumentException("User data not found for userId: " + userId);
//        }
//    }


    public File downloadUserData(String userId) {
        String prefix = "user_" + userId + "/";
        Page<Blob> blobs = storage.list(
                "musefy-export-bucket",
                Storage.BlobListOption.prefix(prefix)
        );

        try {
            Path tempDir = Files.createTempDirectory("userData");
            for (Blob blob : blobs.iterateAll()) {
                String relativePath = blob.getName().replaceFirst(prefix, "");
                Path filePath = tempDir.resolve(relativePath);
                blob.downloadTo(filePath);
            }

            String zipFileName = "user_data_" + userId + ".zip";
            File zipFile = createZipFile(tempDir, zipFileName);
            deleteDirectory(tempDir);

            return zipFile;
        } catch (IOException e) {
            throw new InputOutputException("Error downloading user data" + e.getMessage());
        }
    }

    private File createZipFile(Path directory, String zipFileName) throws IOException {
        File zipFile = new File(zipFileName);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String entryName = directory.relativize(file).toString();
                    zipOutputStream.putNextEntry(new ZipEntry(entryName));
                    Files.copy(file, zipOutputStream);
                    zipOutputStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return zipFile;
    }

    private void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void deleteUserById(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            logger.log(Level.SEVERE, "User not found with ID: {0}", userId);
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        try {
            String folderName = "export/user_" + userId;
            BlobId folderBlobId = BlobId.of("musefy-export-bucket", folderName);
            boolean deleted = storage.delete(folderBlobId);
            if (!deleted) {
                logger.log(Level.WARNING, "Failed to delete folder: {0}", folderName);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error occurred while deleting folder", e);
        }

        userRepository.deleteById(userId);
        logger.info("User deleted successfully");
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
        logger.info("All users deleted successfully");
    }

    public boolean checkIfSongExists(String userId, String songId) {
        return userRepository.existsByIdAndFavouriteSongsContaining(userId, songId);
    }

    public UserDTO updateUser(String userId, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> {
            u.setEmail(userDTO.getEmail());
            u.setFirstName(userDTO.getFirstName());
            u.setLastName(userDTO.getLastName());
            u.setRecommendedSongs(userDTO.getRecommendedSongs());
            u.setFavouriteSongs(userDTO.getUserFavoriteSongs());
            userRepository.save(u);
        });
        logger.log(Level.SEVERE, "User not found with ID: {0} ", userDTO.getId());
        return userDTO;
    }
}
