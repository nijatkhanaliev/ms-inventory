package com.company.messaging;

import com.company.model.events.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String exchange, String routingKey, PaymentFailedEvent event) {
        log.info("Sending payment failed event, orderId {}", event.getOrderId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

}
