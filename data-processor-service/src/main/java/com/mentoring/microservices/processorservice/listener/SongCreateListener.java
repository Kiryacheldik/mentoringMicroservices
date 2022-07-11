package com.mentoring.microservices.processorservice.listener;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SongCreateListener implements RabbitListenerConfigurer {
    @Autowired
    public SongCreateListener(RabbitMqService rabbitMqService) {
        this.rabbitMqService = rabbitMqService;
    }

    private final RabbitMqService rabbitMqService;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.resource-create-queue}")
    @SneakyThrows
    public void processResourceCreation(Long resourceId) {
        log.info("Resource is received " + resourceId);
        rabbitMqService.processResourceCreation(resourceId);
    }
}
