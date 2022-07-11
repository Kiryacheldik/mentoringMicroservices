package com.mentoring.microservices.resourceservice.config;

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

    @Value("${spring.rabbitmq.queues.resource-get-id-queue}")
    private String resourceGetIdQueue;
    @Value("${spring.rabbitmq.routing-keys.resource-get-id-routing-key}")
    private String resourceGetIdRoutingKey;
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.host}")
    public String host;
    @Value("${spring.rabbitmq.username}")
    public String username;
    @Value("${spring.rabbitmq.password}")
    public String password;

    @Bean
    public Queue resourceGetIdQueue() {
        return new Queue(resourceGetIdQueue, true);
    }

    @Bean
    public Exchange resourceExchange() {
        return ExchangeBuilder.directExchange(exchange).durable(true).build();
    }

    @Bean
    public Binding resourceCreatedQueueBinding() {
        return BindingBuilder
                .bind(resourceGetIdQueue())
                .to(resourceExchange())
                .with(resourceGetIdRoutingKey)
                .noargs();
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
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
