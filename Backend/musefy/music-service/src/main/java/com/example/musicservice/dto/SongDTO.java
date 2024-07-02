package com.example.musicservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.HtmlUtils;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SongDTO {
    private String songId;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @NotBlank(message = "Artist cannot be blank")
    @Size(max = 50, message = "Artist cannot exceed 50 characters")
    private String artist;

    @NotBlank(message = "Release date cannot be blank")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Release date must be in the format YYYY-MM-DD")
    private String releaseDate;

    @Min(value = 0, message = "Duration must be a positive number")
    private int duration;

    @NotBlank(message = "Genre cannot be blank")
    @Size(max = 50, message = "Genre cannot exceed 50 characters")
    private String genre;

    private String key;

    @Min(value = 0, message = "Tempo must be a positive number")
    private Integer tempo;

    @NotBlank(message = "MP3 GCP link cannot be blank")
    @Pattern(regexp = "^https?://.*$", message = "MP3 GCP link must be a valid URL")
    private String mp3GCPLink;

    @Pattern(regexp = "^https?://.*$", message = "Image link must be a valid URL")
    private String imageLink;


    public String getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = HtmlUtils.htmlEscape(title);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = HtmlUtils.htmlEscape(artist);
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = HtmlUtils.htmlEscape(genre);
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
}
