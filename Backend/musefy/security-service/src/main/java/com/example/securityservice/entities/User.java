package com.example.securityservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document
@Data
@NoArgsConstructor(force = true)
public class User implements UserDetails {
    @Id
    private String id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Collection<? extends GrantedAuthority> authorities;

    private List<String> favouriteSongs = new ArrayList<>();

    private List<String> recommendedSongs = new ArrayList<>();

    public User (@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NonNull String getUsername() {
        return this.username;
    }

    @Override
    public @NonNull String getPassword() {
        return this.password;
    }

    public String getId() {
        return this.id;
    }

    public List<String> getFavouriteSongs() {
        return favouriteSongs;
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
}