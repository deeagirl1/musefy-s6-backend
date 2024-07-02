package com.example.securityservice.services;
import com.example.securityservice.entities.User;
import com.example.securityservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserDetails user) {

        ((User) user).setPassword(passwordEncoder.encode(user.getPassword()));
        ((User) user).setAuthorities(user.getAuthorities());
        ((User) user).setFavouriteSongs(new ArrayList<>());
        ((User) user).setRecommendedSongs(new ArrayList<>());
        userRepository.save((User) user);
    }

    @Override
    @Transactional
    public void updateUser(UserDetails userDetails) {
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Update password (or other fields as needed)
            user.setUsername(userDetails.getUsername());
            user.setFirstName(userDetails.getUsername());
            user.setLastName(userDetails.getUsername());
            user.setEmail(userDetails.getUsername());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));

            userRepository.save(user);
        } else {
            // Handle the case when no user is found with the provided username
            throw new UsernameNotFoundException("User not found with username {0}:" + userDetails.getUsername());
        }
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // Get currently authenticated user
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
                    "for current user.");
        }

        String username = currentUser.getName();

        // Retrieve the user from the database
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username));

        // Check old password is correct
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect.");
        }

        // Change password
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("username {0} not found", username)
                ));
    }

}