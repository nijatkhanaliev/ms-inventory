package com.company.messaging;

import com.company.model.events.StockUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdatedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String exchange, String routingKey, StockUpdatedEvent event) {
        log.info("Sending stock updated event, eventId {}", event.getEventId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

}
