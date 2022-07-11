package com.mentoring.microservices.processorservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.queues.resource-create-queue}")
    private String resourceCreatedQueue;
    @Value("${spring.rabbitmq.queues.resource-get-content-queue}")
    private String resourceGetContentQueue;
    @Value("${spring.rabbitmq.routing-keys.resource-create-routing-key}")
    private String resourceCreatedRoutingKey;
    @Value("${spring.rabbitmq.routing-keys.resource-get-content-routing-key}")
    private String resourceGetContentRoutingKey;
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Bean
    public Queue resourceCreatedQueue() {
        return new Queue(resourceCreatedQueue, true);
    }

    @Bean
    public Queue resourceGetContentQueue() {
        return new Queue(resourceGetContentQueue, true);
    }

    @Bean
    public Exchange resourceExchange() {
        return ExchangeBuilder.directExchange(exchange).durable(true).build();
    }

    @Bean
    public Binding resourceCreatedQueueBinding() {
        return BindingBuilder
                .bind(resourceCreatedQueue())
                .to(resourceExchange())
                .with(resourceCreatedRoutingKey)
                .noargs();
    }

    @Bean
    public Binding resourceGetContentQueueBinding() {
        return BindingBuilder
                .bind(resourceGetContentQueue())
                .to(resourceExchange())
                .with(resourceGetContentRoutingKey)
                .noargs();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
