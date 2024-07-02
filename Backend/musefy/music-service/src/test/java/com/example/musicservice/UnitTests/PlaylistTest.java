package com.example.musicservice.UnitTests;

import com.example.musicservice.entities.Playlist;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PlaylistTest {

    Playlist playlist;
    static final String PLAYLIST_ID = "1";
    static final String PLAYLIST_TITLE = "Test";
    static final String PLAYLIST_DESCRIPTION = "Test";
    static final String PLAYLIST_CREATED_BY = "Test";

    @BeforeEach
    void setUp(){
        playlist = new Playlist(PLAYLIST_ID, PLAYLIST_TITLE,PLAYLIST_DESCRIPTION, PLAYLIST_CREATED_BY);
    }

    @Test
    public void getPlaylistIdTest()
    {
        Assert.assertEquals(PLAYLIST_ID, playlist.getId());
    }

    @Test
    public void setPlaylistIdTest()
    {
        //arrange
        String newId = "2";
        //act
        playlist.setId(newId);
        //assert
        Assert.assertEquals(newId, playlist.getId());
    }

    @Test
    public void getPlaylistTitleTest()
    {
        Assert.assertEquals(PLAYLIST_TITLE, playlist.getTitle());
    }

    @Test
    public void setPlaylistTitleTest()
    {
        //arrange
        String newTitle = "test2";
        //act
        playlist.setTitle(newTitle);
        //assert
        Assert.assertEquals(newTitle, playlist.getTitle());
    }

    @Test
    public void getPlaylistCreatedByTest()
    {
        Assert.assertEquals(PLAYLIST_CREATED_BY, playlist.getCreatedBy());
    }

    @Test
    public void setPlaylistArtistTest()
    {
        //arrange
        String newUser = "test2";
        //act
        playlist.setCreatedBy(newUser);
        //assert
        Assert.assertEquals(newUser, playlist.getCreatedBy());
    }

    @Test
    public void getPlaylistDescriptionTest()
    {
        Assert.assertEquals(PLAYLIST_DESCRIPTION, playlist.getDescription());
    }

    @Test
    public void setPlaylistDescriptionTest()
    {
        //arrange
        String newDescription = "test_description";
        //act
        playlist.setDescription(newDescription);
        //assert
        Assert.assertEquals(newDescription, playlist.getDescription());
    }

}
