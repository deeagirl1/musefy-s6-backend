package com.example.musicservice.UnitTests;

import com.example.musicservice.entities.Album;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AlbumTest {

    Album album;
    static final String ALBUM_ID = "1";
    static final String ALBUM_TITLE = "Test";
    static final String ALBUM_ARTIST = "Test";

    @BeforeEach
    void setUp(){
        album = new Album(ALBUM_ID, ALBUM_TITLE, ALBUM_ARTIST);
    }

    @Test
    public void getAlbumIdTest()
    {
        Assert.assertEquals(ALBUM_ID, album.getAlbumId());
    }

    @Test
    public void setAlbumIdTest()
    {
        //arrange
        String newId = "2";
        //act
        album.setAlbumId(newId);
        //assert
        Assert.assertEquals(newId, album.getAlbumId());
    }

    @Test
    public void getAlbumTitleTest()
    {
        Assert.assertEquals(ALBUM_TITLE, album.getTitle());
    }

    @Test
    public void setAlbumTitleTest()
    {
        //arrange
        String newTitle = "test2";
        //act
        album.setTitle(newTitle);
        //assert
        Assert.assertEquals(newTitle, album.getTitle());
    }

    @Test
    public void getAlbumArtistTest()
    {
        Assert.assertEquals(ALBUM_ARTIST, album.getArtist());
    }

    @Test
    public void setAlbumArtistTest()
    {
        //arrange
        String newArtist = "test2";
        //act
        album.setTitle(newArtist);
        //assert
        Assert.assertEquals(newArtist, album.getArtist());
    }

}