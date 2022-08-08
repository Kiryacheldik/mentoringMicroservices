package com.mentoring.microservices.processorservice.listener;

import com.mentoring.microservices.processorservice.entity.SongDto;
import com.mentoring.microservices.processorservice.util.SongDataParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@Slf4j
public class RabbitMqService {

    @Autowired
    public RabbitMqService(SongDataParser parser, RabbitTemplate rabbitTemplate) {
        this.parser = parser;
        this.rabbitTemplate = rabbitTemplate;
    }

    private final SongDataParser parser;
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queues.resource-get-content-queue}")
    private String resourceGetContentQueue;
    @Value("${spring.rabbitmq.exchanges.exchange}")
    private String resourceExchange;
    @Value("${spring.rabbitmq.routing-keys.resource-get-id-routing-key}")
    private String resourceGetIdRoutingKey;

    @SneakyThrows
    public void processResourceCreation(Long resourceId) {
        rabbitTemplate.convertAndSend(resourceExchange, resourceGetIdRoutingKey, resourceId);
        Object metaData = rabbitTemplate.receiveAndConvert(resourceGetContentQueue);
        byte[] resourceBytes = SerializationUtils.serialize(metaData);

        SongDto songDto = parser.parseSong(resourceBytes);
        songDto.setResourceId(resourceId);

        URI uri = new URI("http://localhost:8081/api/v1/songs");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(uri, songDto, SongDto.class);
    }
}
