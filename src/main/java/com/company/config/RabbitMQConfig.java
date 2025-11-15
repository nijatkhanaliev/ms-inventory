package com.company.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.company.model.constant.RabbitConstant.STOCK_QUEUE;
import static com.company.model.constant.RabbitConstant.STOCK_ROUTING_KEY;
import static com.company.model.constant.RabbitConstant.STOCK_EXCHANGE;

@Configuration
public class RabbitMQConfig {

    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    public static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    @Bean
    public Queue stockQueue() {
        return QueueBuilder.durable(STOCK_QUEUE)
                .withArgument(X_DEAD_LETTER_EXCHANGE , STOCK_EXCHANGE + ".dlx")
                .withArgument(X_DEAD_LETTER_ROUTING_KEY, STOCK_ROUTING_KEY + ".dlq")
                .build();
    }

    @Bean
    public Queue stockDLQ() {
        return QueueBuilder.durable(STOCK_QUEUE + ".dlq").build();
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(STOCK_EXCHANGE + ".dlx");
    }

    @Bean
    public TopicExchange stockExchange() {
        return new TopicExchange(STOCK_EXCHANGE);
    }

    @Bean
    public Binding bindingOrderCreated(Queue stockQueue, TopicExchange stockExchange) {
        return BindingBuilder.bind(stockQueue)
                .to(stockExchange)
                .with(STOCK_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderDLQ(Queue stockDLQ, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(stockDLQ)
                .to(deadLetterExchange)
                .with(STOCK_ROUTING_KEY + ".dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;
    }

}
