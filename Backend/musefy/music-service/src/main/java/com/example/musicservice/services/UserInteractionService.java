package com.example.musicservice.services;

import autovalue.shaded.org.jetbrains.annotations.NotNull;
import com.example.musicservice.entities.UserSongInteraction;
import com.example.musicservice.repositories.UserInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInteractionService {

    private final UserInteractionRepository interactionRepository;

    @Autowired
    public UserInteractionService(UserInteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    public void incrementPlayCount(@NotNull String userId, @NotNull String songId) {
        UserSongInteraction userInteraction = interactionRepository.findByUserIdAndSongId(userId, songId);
        if (userInteraction == null) {
            userInteraction = new UserSongInteraction();
            userInteraction.setUserId(userId);
            userInteraction.setSongId(songId);
            userInteraction.setPlayCount(1);
        } else {
            int playCount = userInteraction.getPlayCount();
            userInteraction.setPlayCount(playCount + 1);
        }
        interactionRepository.save(userInteraction);
    }

    public List<UserSongInteraction> getAllUserSongInteractionsByUserId(String userId) {
        return interactionRepository.getUserInteractionsByUserId(userId);
    }

    public void deleteByUserId(String userId) {
        interactionRepository.deleteByUserId(userId);
    }

}
