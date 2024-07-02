package com.example.securityservice.dto;

import com.example.securityservice.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonSerialize
@JsonDeserialize
public class UserDTO {
    private String id;

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    public static UserDTO from(User user) {
        return builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();

    }

    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        return user;
    }

    public UserDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ",username='" + username + '\'' +
                '}';
    }
}