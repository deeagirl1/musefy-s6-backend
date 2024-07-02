package com.example.musicservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user_favorite_songs")
public class UserFavoriteSong {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name ="uuid2", strategy = "uuid2")
    private String entityId;
    private String userId;
    private String songId;

    public UserFavoriteSong(String userId, String songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public UserFavoriteSong() {
    }

    public UserFavoriteSong(String entityId, String userId, String songId) {
        this.entityId = entityId;
        this.userId = userId;
        this.songId = songId;
    }
}
