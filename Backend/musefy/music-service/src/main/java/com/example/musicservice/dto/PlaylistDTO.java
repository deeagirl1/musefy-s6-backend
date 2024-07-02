package com.example.musicservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    private String playlistId;
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @NotBlank(message = "Created by cannot be blank")
    @Size(max = 50, message = "Created by cannot exceed 50 characters")
    private String createdBy;

    private String imageLink;
    private List<String> songIds;


    public String getPlaylistId() {
        return playlistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = HtmlUtils.htmlEscape(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = HtmlUtils.htmlEscape(description);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = HtmlUtils.htmlEscape(createdBy);
    }

    public String getImage() {
        return imageLink;
    }

    public void setImage(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
}


