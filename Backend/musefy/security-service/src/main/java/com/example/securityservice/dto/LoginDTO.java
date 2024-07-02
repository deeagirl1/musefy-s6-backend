package com.example.securityservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}