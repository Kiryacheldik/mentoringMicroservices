package com.mentoring.microservices.resourceservice.listener;

import com.mentoring.microservices.resourceservice.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqProcessor {

    @Autowired
    public RabbitMqProcessor(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    private final ResourceService resourceService;

    @RabbitListener(queues = {"${spring.rabbitmq.queues.resource-get-id-queue}"})
    @SendTo(value = {"${spring.rabbitmq.queues.resource-get-content-queue}"})
    //@Retryable
    public byte[] processResourceCreation(Long resourceId) {
        return resourceService.getResource(resourceId).getResourceBytes();
    }
}
