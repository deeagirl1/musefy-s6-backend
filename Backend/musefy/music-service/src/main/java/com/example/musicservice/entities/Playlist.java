package com.example.musicservice.entities;

import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="playlists")
public class Playlist {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name ="uuid2", strategy = "uuid2")
    private String playlistId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "image")
    private String image;

    @ManyToMany(mappedBy = "playlists")
    @JsonIgnore
    private List<Song> songs = new ArrayList<>();

    public Playlist(String playlistId, String title, String description, String createdBy, String image, List<Song> songs) {
        this.playlistId = playlistId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.image = image;
        this.songs = songs;
    }

    public Playlist(String playlistId, String title) {
        this.playlistId = playlistId;
        this.title = title;
    }

    public Playlist(String playlistId, String title, String description, String createdBy) {
        this.playlistId = playlistId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
    }

    public Playlist() {
    }

    public String getId() {
        return playlistId;
    }

    public void setId(String id) {
        this.playlistId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {

        return "Playlist{playlistId=" + playlistId +
                ", title=" + title +
                ", description=" + description +
                ", createdBy=" + createdBy +
                ", songs=" + songs +
                "}";
    }

}
