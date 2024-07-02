package com.example.userservice.dto;

import com.example.userservice.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
@JsonSerialize
@JsonDeserialize
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String id;

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    private List<String> userFavoriteSongs;
    private List<String> recommendedSongs;

    public static UserDTO from(User user) {
        return builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .recommendedSongs(user.getRecommendedSongs())
                .userFavoriteSongs(user.getFavouriteSongs())
                .build();
    }


    public UserDTO(String id, String username) {
        this.id = id;
        this.username = username;

    }

    public UserDTO(String id, String username, String firstName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
    }

    public void sanitizeUserInputs() {
        this.username = HtmlUtils.htmlEscape(this.username);
        this.firstName = HtmlUtils.htmlEscape(this.firstName);
        this.lastName = HtmlUtils.htmlEscape(this.lastName);
    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ",username='" + username + '\'' +
                '}';
    }
}