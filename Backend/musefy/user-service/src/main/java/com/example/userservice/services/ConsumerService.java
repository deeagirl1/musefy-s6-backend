package com.example.userservice.services;

import com.example.userservice.dto.UserRecommendationsDTO;
import com.example.userservice.entities.User;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;


@Service
@EnableAsync
public class ConsumerService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    public ConsumerService(UserRepository userRepository, ObjectMapper objectMapper, UserService userService) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Async
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user.recommendations.queue", durable = "true"),
            exchange = @Exchange(value = "user.recommendations.exchange", type = "topic"),
            key = "user.recommendations.routingkey"
    ))
    public void receiveRecommendations(Message message) {
        try {
            byte[] messageBody = message.getBody();
            String jsonMessage = new String(messageBody);

            UserRecommendationsDTO receivedDto = objectMapper.readValue(jsonMessage, UserRecommendationsDTO.class);
            User user = userRepository.getUserById(receivedDto.getUserId());
            UserRecommendationsDTO recommendationsDTO = new UserRecommendationsDTO();
            if (user != null && user.getRecommendedSongs() != receivedDto.getRecommendedSongs()) {
                String logMessage = String.format("User %s received a new list of recommendations: %s", recommendationsDTO.getUserId(), receivedDto.getRecommendedSongs());
                logger.info(logMessage);
                userService.saveRecommendation(receivedDto.getUserId(), receivedDto.getRecommendedSongs());
            }
        } catch (Exception e) {
            logger.error("Error while receiving song recommendations: " + e.getMessage());
        }
    }
}

