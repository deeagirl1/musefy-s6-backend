package com.example.musicservice.UnitTests;

import com.example.musicservice.dto.SongDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
class SongDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void setTitle_EncodesSpecialCharacters() {
        // Arrange
        SongDTO songDTO = new SongDTO();
        String title = "<script>alert('XSS');</script>";

        // Act
        songDTO.setTitle(title);

        // Assert
        assertEquals("&lt;script&gt;alert(&#39;XSS&#39;);&lt;/script&gt;", songDTO.getTitle());
    }

    @Test
    void setGenre_EncodesSpecialCharacters() {
        // Arrange
        SongDTO songDTO = new SongDTO();
        String genre = "<script>alert('XSS');</script>";

        // Act
        songDTO.setGenre(genre);

        // Assert
        assertEquals("&lt;script&gt;alert(&#39;XSS&#39;);&lt;/script&gt;", songDTO.getGenre());
    }

    @Test
    void setArtist_EscapesHtmlCharacters() {
        // Arrange
        String artist = "<script>alert('XSS');</script>";
        SongDTO songDTO = new SongDTO();

        // Act
        songDTO.setArtist(artist);

        // Assert
        String expected = "&lt;script&gt;alert(&#39;XSS&#39;);&lt;/script&gt;";
        String actual = songDTO.getArtist();
        assertEquals(expected, actual, "Expected artist to be properly set with HTML characters escaped");
    }
    @Test
    void validate_ValidSongDTO_NoValidationErrors() {
        // Arrange
        SongDTO songDTO = new SongDTO();
        songDTO.setTitle("Title");
        songDTO.setArtist("Artist");
        songDTO.setReleaseDate("2023-01-01");
        songDTO.setDuration(180);
        songDTO.setGenre("Pop");
        songDTO.setKey("C");
        songDTO.setTempo(120);
        songDTO.setMp3GCPLink("https://example.com/song.mp3");
        songDTO.setImageLink("https://example.com/image.jpg");

        // Act
        var violations = validator.validate(songDTO);

        // Assert
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void validate_SongDTOWithInvalidData_ValidationErrorsExist() {
        // Arrange
        SongDTO songDTO = new SongDTO();
        songDTO.setTitle(""); // Empty title
        songDTO.setArtist("Artist");
        songDTO.setReleaseDate("2023/01/01"); // Invalid date format
        songDTO.setDuration(-10); // Negative duration
        songDTO.setGenre("Pop");
        songDTO.setKey("C");
        songDTO.setTempo(-120); // Negative tempo
        songDTO.setMp3GCPLink("invalid-url"); // Invalid URL format
        songDTO.setImageLink("https://example.com/image.jpg");

        // Act
        var violations = validator.validate(songDTO);

        // Assert
        assertEquals(5, violations.size());
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("title")));
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("releaseDate")));
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("duration")));
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("tempo")));
    }
}
