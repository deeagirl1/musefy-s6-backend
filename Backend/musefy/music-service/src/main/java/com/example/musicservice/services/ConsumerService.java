package com.example.musicservice.services;

import com.example.musicservice.components.DataExporter;
import com.example.musicservice.dto.UserFavoriteDTO;
import com.example.musicservice.dto.UserInteractionDTO;
import com.example.musicservice.entities.Song;
import com.example.musicservice.entities.UserFavoriteSong;
import com.example.musicservice.repositories.SongRepository;
import com.example.musicservice.repositories.UserFavoriteSongRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@EnableAsync
public class ConsumerService {

    private final SongRepository songRepository;
    private final UserFavoriteSongRepository userFavoriteSongRepository;
    private final UserInteractionService interactionService;
    private final PlaylistService playlistService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataExporter dataExporter;
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    public ConsumerService(SongRepository songRepository, UserFavoriteSongRepository userFavoriteSongRepository, UserInteractionService interactionService, DataExporter dataExporter, PlaylistService playlistService){
        this.songRepository = songRepository;
        this.userFavoriteSongRepository = userFavoriteSongRepository;
        this.interactionService = interactionService;
        this.dataExporter = dataExporter;
        this.playlistService = playlistService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "music.queue", durable = "true"),
            exchange = @Exchange(value = "music.exchange", type = "direct"),
            key = "music.routingkey"
    ))
    @Async
    public void receivedMessage(String jsonMessage) throws JsonProcessingException {
        UserFavoriteDTO dtoObject = objectMapper.readValue(jsonMessage, UserFavoriteDTO.class);
        Optional<Song> song = songRepository.findById(dtoObject.getSongId());
        if(song.isEmpty()){
            logger.error("Song with id " + dtoObject.getSongId() + " not found");
            return;
        }
        UserFavoriteSong userFavoriteSong = new UserFavoriteSong(dtoObject.getUserId(), song.get().getSongId());
        userFavoriteSongRepository.save(userFavoriteSong);
        logger.info("Song " + song.get().getSongId() + "has been added to " + dtoObject.getUserId() + "'s favorite songs list");

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "deletes.queue", durable = "true"),
            exchange = @Exchange(value = "deletes.exchange", type = "topic"),
            key = "deletes.routingkey"
    ))
    @Transactional
    @Async
    public void receiveDeleteAccountMessage(String jsonMessage) throws JsonProcessingException {
        String userId = objectMapper.readValue(jsonMessage, String.class);
        userFavoriteSongRepository.deleteByUserId(userId);
        interactionService.deleteByUserId(userId);
        playlistService.deleteByCreatedBy(userId);
        logger.info("User account has been deleted: {}", userId);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "export.queue", durable = "true"),
            exchange = @Exchange(value = "export.exchange", type = "topic"),
            key = "export.routingkey"
    ))
    @Transactional
    @Async
    public void receiveExportUserDataMessage(String jsonMessage) throws JsonProcessingException {
        String userId = objectMapper.readValue(jsonMessage, String.class);
        dataExporter.uploadUserDataToGCP(userId);
        logger.info("User interactions has been exported to GCP: {}", userId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user-song-interaction-queue", durable = "true"),
            exchange = @Exchange(value = "interaction.exchange", type = "topic"),
            key = "interaction.routingkey"
    ))
    @Async
    public void receiveUserSongInteractionEvent(String jsonMessage) throws JsonProcessingException
        {
            UserInteractionDTO dtoObject = objectMapper.readValue(jsonMessage, UserInteractionDTO.class);
            logger.info("User " + dtoObject.getUserId() + " has listened to song " + dtoObject.getSongId());
            logger.info("Incrementing interaction");
            interactionService.incrementPlayCount(dtoObject.getUserId(), dtoObject.getSongId());
            logger.info("Interaction incremented");
        }

}
