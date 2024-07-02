package com.example.userservice.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
public class User {

    @Id
    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private List<String> favouriteSongs = new ArrayList<>();

    private List<String> recommendedSongs = new ArrayList<>();

    public String getId() {
        return this.id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getFavouriteSongs() {
        return favouriteSongs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFavouriteSongs(List<String> favouriteSongs) {
        this.favouriteSongs = favouriteSongs;
    }

    public List<String> getRecommendedSongs() {
        return recommendedSongs;
    }

    public void setRecommendedSongs(List<String> recommendedSongs) {
        this.recommendedSongs = recommendedSongs;
    }



    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}