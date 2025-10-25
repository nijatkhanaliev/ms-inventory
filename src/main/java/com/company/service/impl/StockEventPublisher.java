package com.company.service.impl;

import com.company.dao.entity.Product;
import com.company.dao.repository.ProductRepository;
import com.company.exception.InvalidOrderItemsException;
import com.company.exception.NotFoundException;
import com.company.messaging.PaymentFailedProducer;
import com.company.messaging.StockFailedProducer;
import com.company.messaging.StockUpdatedProducer;
import com.company.model.dto.OrderItemDto;
import com.company.model.events.OrderCreatedEvent;
import com.company.model.events.PaymentFailedEvent;
import com.company.model.events.StockFailedEvent;
import com.company.model.events.StockUpdatedEvent;
import com.company.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.company.config.RabbitMQConfig.ORDER_EXCHANGE;
import static com.company.config.RabbitMQConfig.ORDER_PAYMENT_FAILED_ROUTING_KEY;
import static com.company.config.RabbitMQConfig.PAYMENT_FAILED_ROUTING_KEY;
import static com.company.config.RabbitMQConfig.STOCK_FAILED_ROUTING_KEY;
import static com.company.config.RabbitMQConfig.STOCK_UPDATED_ROUTING_KEY;
import static com.company.exception.constant.ErrorCode.DATA_NOT_FOUND;
import static com.company.exception.constant.ErrorCode.INVALID_ORDER_ITEMS;
import static com.company.exception.constant.ErrorMessage.DATA_NOT_FOUND_MESSAGE;
import static com.company.exception.constant.ErrorMessage.INVALID_ORDER_ITEMS_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventPublisher {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final StockFailedProducer stockFailedProducer;
    private final StockUpdatedProducer stockUpdatedProducer;
    private final PaymentFailedProducer paymentFailedProducer;

    public void handleStockUpdatedFailed(Long orderId, String exceptionMessage) {
        log.error("Failed to process order.created event. message: {}", exceptionMessage);
        StockFailedEvent stockFailedEvent = new StockFailedEvent();
        stockFailedEvent.setOrderId(orderId);
        stockFailedEvent.setReason(exceptionMessage);
        stockFailedProducer.send(ORDER_EXCHANGE, STOCK_FAILED_ROUTING_KEY, stockFailedEvent);
    }

    @Transactional
    public void handleStockUpdated(OrderCreatedEvent event) {
        log.info("Processing order created event, eventId: {}", event.getEventId());
        List<OrderItemDto> orderItemEventList = event.getOrderItemDtos();

        if (orderItemEventList.isEmpty()) {
            log.error("Order.Item.Event is empty, orderId {}", event.getOrderId());
            throw new InvalidOrderItemsException(INVALID_ORDER_ITEMS_MESSAGE,
                    INVALID_ORDER_ITEMS);
        }
        orderItemEventList.forEach((item) -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND_MESSAGE,
                            DATA_NOT_FOUND));
            productService.updateStock(item.getProductId(),
                    (product.getStock() - item.getQuantity()));
        });

        StockUpdatedEvent stockUpdatedEvent = new StockUpdatedEvent();
        stockUpdatedEvent.setOrderId(event.getOrderId());
        stockUpdatedEvent.setUserId(event.getUserId());
        stockUpdatedEvent.setTotalPrice(event.getTotalPrice());
        stockUpdatedEvent.setOrderItemDtos(event.getOrderItemDtos());
        stockUpdatedProducer.send(ORDER_EXCHANGE, STOCK_UPDATED_ROUTING_KEY, stockUpdatedEvent);
    }

    @Transactional
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Handling payment failed. Restore stock in inventory. orderId {}", event.getOrderId());
        List<OrderItemDto> orderItemDtos = event.getOrderItemDtos();
        orderItemDtos.forEach((item) -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException(
                                    DATA_NOT_FOUND_MESSAGE, DATA_NOT_FOUND
                            )
                    );
            productService.updateStock(item.getProductId(),
                    (product.getStock() + item.getQuantity()));
        });



        paymentFailedProducer.send(ORDER_EXCHANGE, ORDER_PAYMENT_FAILED_ROUTING_KEY, event);
    }

}
