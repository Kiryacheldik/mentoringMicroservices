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
    @Value("${spring.rabbitmq.exchanges.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.queues.resource-dlq}")
    private String resourceDlq;
    @Value("${spring.rabbitmq.routing-keys.resource-dlq-routing-key}")
    private String resourceDlqRoutingKey;
    @Value("${spring.rabbitmq.exchanges.dlq-exchange}")
    private String exchangeDlq;
    @Value("${spring.rabbitmq.host}")
    public String host;
    @Value("${spring.rabbitmq.username}")
    public String username;
    @Value("${spring.rabbitmq.password}")
    public String password;

    @Bean
    public Queue resourceGetIdQueue() {
        return QueueBuilder.durable(resourceGetIdQueue)
                .withArgument("x-dead-letter-exchange", exchangeDlq)
                .withArgument("x-dead-letter-routing-key", resourceDlqRoutingKey)
                .build();
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
    public Queue resourceDlqQueue() {
        return new Queue(resourceDlq, true);
    }

    @Bean
    public Exchange resourceDlqExchange() {
        return ExchangeBuilder.directExchange(exchangeDlq).durable(true).build();
    }

    @Bean
    public Binding resourceDlqBinding() {
        return BindingBuilder
                .bind(resourceDlqQueue())
                .to(resourceDlqExchange())
                .with(resourceDlqRoutingKey)
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
