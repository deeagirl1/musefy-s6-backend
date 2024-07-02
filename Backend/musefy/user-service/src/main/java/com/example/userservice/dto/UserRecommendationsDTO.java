package com.example.userservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@RequiredArgsConstructor
public class UserRecommendationsDTO {

    @NotBlank
    private String userId;

    private List<String> recommendedSongs;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRecommendedSongs() {
        return recommendedSongs;
    }

    public void setRecommendedSongs(List<String> recommendedSongs) {
        this.recommendedSongs = recommendedSongs;
    }

    public UserRecommendationsDTO(String userId, List<String> recommendedSongs) {
        this.userId = userId;
        this.recommendedSongs = recommendedSongs;
    }

    public void sanitizeUserInputs() {
        this.userId = HtmlUtils.htmlEscape(this.userId);
        if (recommendedSongs != null) {
            recommendedSongs.replaceAll(HtmlUtils::htmlEscape);
        }
    }
}

