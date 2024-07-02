package com.example.userservice.services;

import com.example.userservice.dto.UserFavoriteSongDTO;
import com.example.userservice.dto.UserInteractionDTO;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Value("${spring.rabbitmq.deletes.exchange}")
    private String deleteExchange;

    @Value("${spring.rabbitmq.deletes.routingkey}")
    private String deleteRoutingKey;

    @Value("${spring.rabbitmq.interaction.exchange}")
    private String interactionExchange;

    @Value("${spring.rabbitmq.interaction.routingkey}")
    private String interactionRoutingKey;

    @Value("${spring.rabbitmq.export.exchange}")
    private String exportExchange;

    @Value("${spring.rabbitmq.export.routingkey}")
    private String exportRoutingKey;

    @Autowired
    public ProducerService(RabbitTemplate rabbitTemplate, UserRepository userRepository, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Async
    public void sendAddSongMessage(UserFavoriteSongDTO message) throws JsonProcessingException {
        UserFavoriteSongDTO dtoObject = new UserFavoriteSongDTO();
        dtoObject.setUserId(message.getUserId());
        dtoObject.setSongId(message.getSongId());
        String json = objectMapper.writeValueAsString(dtoObject);
        String logMessage = String.format("Sending 'Add Song' message to RabbitMQ: [userId: %s, songId: %s]", dtoObject.getUserId(), dtoObject.getSongId());
        logger.info(logMessage);
        rabbitTemplate.convertAndSend(exchange, routingkey, json);
    }

    @Async
    public void sendDeleteAccountMessage(String userId) throws JsonProcessingException {
        // Check if user exists
        if (userRepository.existsById(userId)) {
            String json = objectMapper.writeValueAsString(userId);
            String logMessage = String.format("Sending 'Delete Account' message to RabbitMQ: [userId: %s]", userId);
            logger.info(logMessage);
            rabbitTemplate.convertAndSend(deleteExchange, deleteRoutingKey, json);
        } else {
            logger.info("User does not exist: {}", userId);
        }
    }

    @Async
    public void sendExportUserDataMessage(String userId) throws JsonProcessingException {
        // Check if user exists
        if (userRepository.existsById(userId)) {
            String json = objectMapper.writeValueAsString(userId);
            String logMessage = String.format("Sending 'Export User data' message to RabbitMQ: [userId: %s]", userId);
            logger.info(logMessage);
            rabbitTemplate.convertAndSend(exportExchange, exportRoutingKey, json);
        } else {
            logger.info("User does not exist: {}", userId);
        }
    }

    @Async
    public void sendUserSongListenedEvent(UserInteractionDTO dto) throws JsonProcessingException {
        UserInteractionDTO dtoObject = new UserInteractionDTO();
        dtoObject.setUserId(dto.getUserId());
        dtoObject.setSongId(dto.getSongId());
        String json = objectMapper.writeValueAsString(dtoObject);
        String logMessage = String.format("Sending 'User Song Listened' event to RabbitMQ: [userId: %s, songId: %s]", dtoObject.getUserId(), dtoObject.getSongId());
        logger.info(logMessage);
        rabbitTemplate.convertAndSend(interactionExchange, interactionRoutingKey, json);
    }

}

