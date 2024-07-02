package com.example.musicservice.UnitTests;

import com.example.musicservice.entities.Song;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SongTest {

    Song song;
    static final String SONG_ID = "1";
    static final String SONG_TITLE = "Test";
    static final String SONG_ARTIST = "Test";
    static final String SONG_RELEASE_DATE = "05.05.2023";
    static final int SONG_DURATION = 123;

    @BeforeEach
    void setUp(){
        song = new Song(SONG_ID, SONG_TITLE,SONG_ARTIST,SONG_RELEASE_DATE, SONG_DURATION);
    }

    @Test
    public void getSongIdTest()
    {
        Assert.assertEquals(SONG_ID, song.getSongId());
    }

    @Test
    public void setSongIdTest()
    {
        //arrange
        String newId = "2";
        //act
        song.setSongId(newId);
        //assert
        Assert.assertEquals(newId, song.getSongId());
    }

    @Test
    public void getSongTitleTest()
    {
        Assert.assertEquals(SONG_TITLE, song.getTitle());
    }

    @Test
    public void setSongTitleTest()
    {
        //arrange
        String newTitle = "test2";
        //act
        song.setTitle(newTitle);
        //assert
        Assert.assertEquals(newTitle, song.getTitle());
    }

    @Test
    public void getSongReleaseDate()
    {
        Assert.assertEquals(SONG_RELEASE_DATE, song.getReleaseDate());
    }

    @Test
    public void setSongReleaseDateTest()
    {
        //arrange
        String newReleaseDate = "07.08.2023";
        //act
        song.setReleaseDate(newReleaseDate);
        //assert
        Assert.assertEquals(newReleaseDate, song.getReleaseDate());
    }

    @Test
    public void getSongDurationTest()
    {
        Assert.assertEquals(SONG_DURATION,song.getDuration());
    }

    @Test
    public void setPlaylistDescriptionTest()
    {
        //arrange
        int newDuration = 12;
        //act
        song.setDuration(newDuration);
        //assert
        Assert.assertEquals(SONG_DURATION, song.getDuration());
    }

}
