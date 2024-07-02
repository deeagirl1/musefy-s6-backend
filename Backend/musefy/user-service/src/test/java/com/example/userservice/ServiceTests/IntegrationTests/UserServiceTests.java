package com.example.userservice.ServiceTests.IntegrationTests;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserFavoriteSongDTO;
import com.example.userservice.entities.User;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.UserService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Storage storage;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUsers() {
        User user1 = new User("1", "john", "doe");
        User user2 = new User("2", "jane", "smith");
        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("john", result.get(0).getFirstName());
        assertEquals("doe", result.get(0).getLastName());
        assertEquals("2", result.get(1).getId());
        assertEquals("jane", result.get(1).getFirstName());
        assertEquals("smith", result.get(1).getLastName());
    }

    @Test
    void testGetUserById() {
        User user = new User("1", "john", "doe");
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById("1");

        assertEquals("1", result.getId());
        assertEquals("john", result.getFirstName());
        assertEquals("doe", result.getLastName());
    }

    @Test
    void testAddSongToFavouriteList() {
        String userId = "1";
        String songId = "123";
        User user = new User(userId, "john", "doe");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserFavoriteSongDTO userFavoriteSongDTO = new UserFavoriteSongDTO(userId, songId);
        userService.addSongToFavouriteList(userFavoriteSongDTO);

        assertEquals(1, user.getFavouriteSongs().size());
        assertEquals(songId, user.getFavouriteSongs().get(0));
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void testGetUserFavoriteSongs() {
        String userId = "1";
        List<String> favoriteSongs = Arrays.asList("123", "456");
        User user = new User(userId, "john", "doe");
        user.setFavouriteSongs(favoriteSongs);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<String> result = userService.getUserFavoriteSongs(userId);

        assertEquals(2, result.size());
        assertEquals("123", result.get(0));
        assertEquals("456", result.get(1));
    }

    @Test
    void testDeleteUserById() {
        String userId = "1";
        User user = new User(userId, "john", "doe");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(storage.delete(Mockito.any(BlobId.class))).thenReturn(true);

        userService.deleteUserById(userId);

        Mockito.verify(userRepository).deleteById(userId);
        Mockito.verify(storage).delete(Mockito.any(BlobId.class));
    }

    @Test
    void testCheckIfSongExists() {
        String userId = "1";
        String songId = "123";
        Mockito.when(userRepository.existsByIdAndFavouriteSongsContaining(userId, songId)).thenReturn(true);

        boolean result = userService.checkIfSongExists(userId, songId);

        assertTrue(result);
    }
}


