package com.example.userservice.ServiceTests.UnitTests;

import com.example.userservice.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserTest {

    User user;
    static final String USER_ID = "1";
    static final String USER_USERNAME = "Test";
    static final String USER_PASSWORD = "Test";

    @BeforeEach
    void setUp(){
        user = new User(USER_ID, USER_USERNAME, USER_PASSWORD);
    }

    @Test
    public void getIdTest()
    {
        Assert.assertEquals(USER_ID, user.getId());
    }

    @Test
    public void setIdTest()
    {
        //arrange
        String newId = "2";
        //act
        user.setId(newId);
        //assert
        Assert.assertEquals(newId, user.getId());
    }

    @Test
    public void getUsernameTest()
    {
        Assert.assertEquals(USER_USERNAME, user.getUsername());
    }

    @Test
    public void setUsernameTest()
    {
        //arrange
        String newUsername = "test2";
        //act
        user.setUsername(newUsername);
        //assert
        Assert.assertEquals(newUsername, user.getUsername());
    }

}
