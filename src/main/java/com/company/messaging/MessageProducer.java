package com.company.messaging;

import com.company.model.events.PaymentFailedEvent;
import com.company.model.events.StockFailedEvent;
import com.company.model.events.StockUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendStockFailed(String exchange, String routingKey, StockFailedEvent event) {
        log.info("Sending stock failed event, eventId {}", event.getEventId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

    public void sendStockUpdated(String exchange, String routingKey, StockUpdatedEvent event) {
        log.info("Sending stock updated event, eventId {}", event.getEventId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

    public void sendPaymentFailed(String exchange, String routingKey, PaymentFailedEvent event) {
        log.info("Sending payment failed event, orderId {}", event.getOrderId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

}
