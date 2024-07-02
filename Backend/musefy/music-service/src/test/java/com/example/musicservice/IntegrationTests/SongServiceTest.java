package com.example.musicservice.IntegrationTests;

import com.example.musicservice.entities.Album;
import com.example.musicservice.entities.Song;
import com.example.musicservice.repositories.SongRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(value = "test")
class SongServiceTest {
    @Mock
    SongRepository songRepository;

    @Test
    void getSongByIdTest()
    {
        String songId = "1";
        String albumId = "1";
        Album album = new Album(albumId, "test", "test",new ArrayList<>());
        Song song = new Song(songId, "Test", "Test",album,"Test",23,"test");
        //act
        when(songRepository.getById(songId)).thenReturn(song);
        //assert
        Assert.assertEquals(songId, songRepository.getById("1").getSongId());
    }

    @Test
    void getAllSongsTest()
    {
        //arrange
        Album album = new Album("1", "test", "test",new ArrayList<>());
        Song song1 = new Song("1", "Test", "Test",album,"Test",23,"test");
        Song song2 = new Song("2", "Test", "Test",album,"Test",23,"test");
        //act
        when(songRepository.findAll()).thenReturn(Arrays.asList(song1, song2));
        //assert
        Assert.assertEquals(Arrays.asList(song1, song2), songRepository.findAll());
    }

    @Test
    void createSong()
    {
        //arrange
        Album album = new Album("1", "test", "test",new ArrayList<>());
        Song song = new Song("1", "Test", "Test",album,"Test",23,"test");
        //act
        when(songRepository.save(any(Song.class))).thenReturn(song);
        //assert
        Assert.assertEquals(song, songRepository.save(song));
    }

    @Test
    void removeSong()
    {
        // arrange
        Album album = new Album("1", "test", "test",new ArrayList<>());
        Song song = new Song("1", "Test", "Test",album,"Test",23,"test");
        // act
        songRepository.save(song);
        songRepository.deleteById(song.getSongId());
        // assert
        Assert.assertEquals(Optional.empty(), songRepository.findById(song.getSongId()));
    }
}