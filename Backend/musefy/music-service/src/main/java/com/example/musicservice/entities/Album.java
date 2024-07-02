package com.example.musicservice.entities;

import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name="albums")
public class Album {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name ="uuid2", strategy = "uuid2")
    private String albumId;
    @Column(name="title")
    private String title;
    @Column(name="artist")
    private String artist;
    @OneToMany(mappedBy = "album")
    @JsonIgnore
    private List<Song> songs;


    public Album(String albumId, String title, String artist, List<Song> songs) {
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
        this.songs = songs;
    }

    public Album(String albumId, String title, String artist) {
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
    }

    public Album() {
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {

        return "Album{albumId=" + albumId +
                ", title=" + title +
                ", artist=" + artist +
                ", songs=" + songs +
                "}";
    }
}
