package com.example.musicservice.IntegrationTests;

import com.example.musicservice.entities.Album;
import com.example.musicservice.entities.Song;
import com.example.musicservice.repositories.AlbumRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(value = "test")
class AlbumServiceTest {
    @Mock
    AlbumRepository albumRepository;

    @Test
    void getAlbumByIdTest()
    {
        String albumId = "1";
        Album album = new Album(albumId, "Test", "Test");
        //act
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));
        //assert
        Assert.assertEquals(albumId, albumRepository.findById("1").get().getAlbumId());
    }

    @Test
    void getAllAlbumsTest()
    {
        //arrange
        String albumId = "1";

        Album album1 = new Album("1", "test", "test",new ArrayList<>());
        Album album2 = new Album("2", "test", "test",new ArrayList<>());
        List<Song> songsAlbum1 = new ArrayList<>() {
            final Song song1 = new Song("1", "Test", "Test",album1,"Test",23,"test");
            final Song song2 = new Song("2", "Test", "Test",album1,"Test",23,"test");
        };
        album1.setSongs(songsAlbum1);

        List<Song> songsAlbum2 = new ArrayList<>() {
            final Song song1 = new Song("1", "Test", "Test","test", 23);
            final Song song2 = new Song("2", "Test", "Test","test", 23);
        };

        album2.setSongs(songsAlbum2);
        //act
        when(albumRepository.findAll()).thenReturn(Arrays.asList(album1, album2));
        //assert
        Assert.assertEquals(Arrays.asList(album1, album2), albumRepository.findAll());
    }

    @Test
    void createAlbumTest()
    {
        //arrange
        Album album = new Album("1", "test", "test",new ArrayList<>());
        List<Song> songs = new ArrayList<>() {
            final Song song1 = new Song("1", "Test", "Test",album,"Test",23,"test");
        };

        album.setSongs(songs);
        //act
        when(albumRepository.save(any(Album.class))).thenReturn(album);
        //assert
        Assert.assertEquals(album, albumRepository.save(album));
    }

    @Test
    void deleteAlbumTest()
    {
        // arrange
        Album album = new Album("1", "test", "test",new ArrayList<>());
        List<Song> songs = new ArrayList<>() {
            final Song song1 = new Song("1", "Test", "Test",album,"Test",23,"test");
        };

        album.setSongs(songs);
        // act
        albumRepository.save(album);
        albumRepository.deleteById(album.getAlbumId());
        // assert
        Assert.assertEquals(Optional.empty(), albumRepository.findById(album.getAlbumId()));
    }
}
