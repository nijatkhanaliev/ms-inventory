package com.company.messaging;

import com.company.common.BaseResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrderCreatedResult(String exchange, String routingKey, BaseResultEvent event) {
        log.info("Sending stock update result event, eventId {}", event.getEventId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

}
