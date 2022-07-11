package com.mentoring.microservices.resourceservice.listener;

import com.mentoring.microservices.resourceservice.service.ResourceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqResourceListener {

    @Autowired
    public RabbitMqResourceListener(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    private final ResourceService resourceService;

    @RabbitListener(queues = "${spring.rabbitmq.queues.resource-get-id-queue}")
    @SendTo("${spring.rabbitmq.queues.resource-get-content-queue}")
    public byte[] processResourceCreation(Long resourceId) {
        return resourceService.getResource(resourceId).getResourceBytes();
    }
}
