package com.example.musicservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="songs")
@AllArgsConstructor
public class Song {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name ="uuid2", strategy = "uuid2")
    private String songId;

    @Column(name="title")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @Column(name="artist")
    @NotEmpty(message = "Artist cannot be empty")
    private String artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @JsonIgnore
    private Album album;

    @Column(name="release_date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Release date must be in the format YYYY-MM-DD")
    private String releaseDate;

    @Min(0)
    @Column(name = "duration")
    private int duration;

    @NotEmpty(message = "Genre cannot be empty")
    @Column(name = "genre")
    private String genre;

    @Column(name = "song_key")
    private String key;

    @Min(0)
    @Column(name = "tempo")
    private Integer tempo;

    @Column(name = "mp3_GCP_link")
    @Pattern(regexp = "^https?://.*$", message = "MP3 GCP link must be a valid URL")
    @NotEmpty
    private String mp3GCPLink;

    @Pattern(regexp = "^https?://.*$", message = "MP3 GCP link must be a valid URL")
    @Column(name = "image_link")
    private String imageLink;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "playlists_song",
            joinColumns = { @JoinColumn(name = "song_id") },
            inverseJoinColumns = { @JoinColumn(name = "playlist_id") }
    )
    @JsonIgnore
    private List<Playlist> playlists = new ArrayList<>();


    public Song(String songId, String title, String artist, Album album, String releaseDate, int duration, String mp3GCPLink) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mp3GCPLink = mp3GCPLink;
    }

    public Song(String songId, String title) {
        this.songId = songId;
        this.title = title;
    }

    public Song(String songId, String title, String artist, String releaseDate, int duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Song(String songId, String title, String artist, Album album, String key, Integer tempo, int duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.key = key;
        this.tempo = tempo;
        this.duration = duration;
    }

    public Song(String songId, String title, String artist, Album album, String releaseDate, String genre, int duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.duration = duration;
    }



    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMp3GCPLink() {
        return mp3GCPLink;
    }

    public void setMp3GCPLink(String mp3GCPLink) {
        this.mp3GCPLink = mp3GCPLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public Song()
    {

    }

    @Override
    public String toString() {

        return "Song{title=" + title +
                ", artist=" + artist +
                ", album=" + album +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", genre=" + genre +
                ", key=" + key +
                ", tempo=" + tempo +
                ", mp3GCPLink=" + mp3GCPLink +
                ", imageLink=" + imageLink +
                ", playlists=" + playlists +
                "}";
    }

}
