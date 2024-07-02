package com.example.userservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class UserFavoriteSongDTO {

    @NotBlank
    private String userId;
    @NotBlank
    private String songId;

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

    public UserFavoriteSongDTO(String userId, String songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public void sanitizeUserInputs() {
        this.userId = HtmlUtils.htmlEscape(this.userId);
        this.songId = HtmlUtils.htmlEscape(this.songId);
    }
}
