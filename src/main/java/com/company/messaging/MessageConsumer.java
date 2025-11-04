package com.company.messaging;

import com.company.exception.InsufficientStockException;
import com.company.exception.InvalidOrderItemsException;
import com.company.exception.NotFoundException;
import com.company.model.events.OrderCreatedEvent;
import com.company.model.events.PaymentFailedEvent;
import com.company.service.impl.StockEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.company.config.RabbitMQConfig.ORDER_CREATED_QUEUE;
import static com.company.config.RabbitMQConfig.PAYMENT_FAILED_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final StockEventPublisher stockEventPublisher;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    private void handleOrderCreated(OrderCreatedEvent event) {
        try {
            stockEventPublisher.handleStockUpdated(event);
        } catch (InvalidOrderItemsException | NotFoundException | InsufficientStockException ex) {
            stockEventPublisher.handleStockUpdatedFailed(event.getOrderId(), ex.getMessage());
        } catch (Exception ex) {
            log.error("ORDER.CREATED.EVENT in inventory, exception happened. Message '{}'", ex.getMessage());
            stockEventPublisher.handleStockUpdatedFailed(event.getOrderId(), ex.getMessage());
            throw ex;
        }
    }


    @RabbitListener(queues = PAYMENT_FAILED_QUEUE)
    private void consumePaymentFailed(PaymentFailedEvent event) {
        log.info("ORDER.CREATED.PAYMENT.FAILED, orderId {}, reason '{}'",
                event.getOrderId(), event.getReason());

        stockEventPublisher.handlePaymentFailed(event);
    }

}
