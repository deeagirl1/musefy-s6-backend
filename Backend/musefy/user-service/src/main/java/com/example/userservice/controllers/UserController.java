package com.example.userservice.controllers;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserFavoriteSongDTO;
import com.example.userservice.dto.UserInteractionDTO;
import com.example.userservice.services.ProducerService;
import com.example.userservice.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;
    private final ProducerService producerService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, ProducerService producerService) {
        this.userService = userService;
        this.producerService = producerService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @Async
    @PutMapping("/update/{userId}")
    public CompletableFuture<ResponseEntity<UserDTO>> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return CompletableFuture.completedFuture(ResponseEntity.ok(updatedUser));
    }

    @Async
    @DeleteMapping("/delete")
    public CompletableFuture<ResponseEntity<Void>> deleteUser(@RequestParam("userId") String userId) throws JsonProcessingException {
        producerService.sendDeleteAccountMessage(userId);
        logger.info("User has been deleted: {}", userId);
        userService.deleteUserById(userId);
        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }

    @Async
    @PostMapping("/add-song-to-favourite-list")
    public CompletableFuture<ResponseEntity<String>> addSongToFavouriteList(@RequestBody UserFavoriteSongDTO userFavoriteSongDTO) throws JsonProcessingException {
        if (userService.checkIfSongExists(userFavoriteSongDTO.getUserId(), userFavoriteSongDTO.getSongId())) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Song already exists in the user's favorite songs list"));
        }

        producerService.sendAddSongMessage(userFavoriteSongDTO);
        logger.info("User with ID: {} has added song with ID: {} to their favorite songs list", userFavoriteSongDTO.getUserId(), userFavoriteSongDTO.getSongId());
        userService.addSongToFavouriteList(userFavoriteSongDTO);
        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }

    @GetMapping("/{userId}/favorite-songs")
    public ResponseEntity<List<String>> getUserFavoriteSongs(@PathVariable String userId) {
        List<String> favoriteSongs = userService.getUserFavoriteSongs(userId);
        return ResponseEntity.ok(favoriteSongs);
    }

    @GetMapping("/{userId}/download-data")
    public ResponseEntity<InputStreamResource> downloadUserData(@PathVariable String userId) throws IOException {
        File zipFile = userService.downloadUserData(userId);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));

        return ResponseEntity.ok()
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Async
    @PostMapping("/song-listened")
    public CompletableFuture<ResponseEntity<String>> handleSongListenedEvent(@RequestBody UserInteractionDTO dto) throws JsonProcessingException {
        producerService.sendUserSongListenedEvent(dto);
        return CompletableFuture.completedFuture(ResponseEntity.ok("User-song interaction event handled successfully"));
    }

    @Async
    @DeleteMapping("/delete-all")
    public CompletableFuture<ResponseEntity<String>> deleteAllUsers() {
        userService.deleteAllUsers();
        return CompletableFuture.completedFuture(ResponseEntity.ok("All users have been deleted"));
    }
}

