package com.example.musicservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {

    private String albumId;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Artist cannot be blank")
    @Size(max = 255, message = "Artist cannot exceed 255 characters")
    private String artist;

    private List<String> songsIds;
}
