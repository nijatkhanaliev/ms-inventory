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

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_CREATED_QUEUE = "order-created-queue";
    public static final String PAYMENT_FAILED_QUEUE = "payment-failed-queue";
    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_ROUTING_KEY = "order.created";
    public static final String STOCK_UPDATED_ROUTING_KEY = "stock.updated";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.failed";
    public static final String STOCK_FAILED_ROUTING_KEY = "stock.failed";
    public static final String ORDER_PAYMENT_FAILED_ROUTING_KEY = "order.payment.failed";

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_EXCHANGE + ".dlx")
                .withArgument("x-dead-letter-routing-key", ORDER_ROUTING_KEY + ".dlq")
                .build();
    }

    @Bean
    public Queue orderDLQ() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE + ".dlq").build();
    }

    @Bean
    public Queue paymentFailedQueue() {
        return QueueBuilder.durable(PAYMENT_FAILED_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_EXCHANGE + ".dlx")
                .withArgument("x-dead-letter-routing-key", PAYMENT_FAILED_ROUTING_KEY + ".dlq")
                .build();
    }

    @Bean
    public Queue paymentFailedDLQ() {
        return QueueBuilder.durable(PAYMENT_FAILED_QUEUE + ".dlq").build();
    }


    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(ORDER_EXCHANGE + ".dlx");
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding bindingOrder(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderDLQ(Queue orderDLQ, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(orderDLQ)
                .to(deadLetterExchange)
                .with(ORDER_ROUTING_KEY + ".dlq");
    }

    @Bean
    public Binding bindingPaymentFailedQueue(Queue paymentFailedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(paymentFailedQueue)
                .to(orderExchange)
                .with(PAYMENT_FAILED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingPaymentFailedDLQ(Queue paymentFailedDLQ, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(paymentFailedDLQ)
                .to(deadLetterExchange)
                .with(PAYMENT_FAILED_ROUTING_KEY + ".dlq");
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
