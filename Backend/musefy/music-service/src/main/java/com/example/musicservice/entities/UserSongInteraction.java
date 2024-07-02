package com.example.musicservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user_song_interactions")
public class UserSongInteraction {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name ="uuid2", strategy = "uuid2")
    private String userSongInteractionId;
    private String userId;
    private String songId;
    private int playCount;

    public UserSongInteraction() {
    }

    public UserSongInteraction(String userId, String songId, int playCount) {
        this.userId = userId;
        this.songId = songId;
        this.playCount = playCount;
    }

    public UserSongInteraction(String userId, String songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public String getUserSongInteractionId() {
        return userSongInteractionId;
    }

    public void setUserSongInteractionId(String userSongInteractionId) {
        this.userSongInteractionId = userSongInteractionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}