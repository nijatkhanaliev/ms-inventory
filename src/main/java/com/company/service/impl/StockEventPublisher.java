package com.company.service.impl;

import com.company.common.BaseResultEvent;
import com.company.dao.entity.Product;
import com.company.dao.repository.ProductRepository;
import com.company.exception.InvalidOrderItemsException;
import com.company.exception.NotFoundException;
import com.company.messaging.MessageProducer;
import com.company.model.dto.OrderDto;
import com.company.model.dto.OrderItemDto;
import com.company.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final MessageProducer messageProducer;

    @Transactional
    public void updateStockTransactional(OrderDto orderDto) {
        List<OrderItemDto> orderItemEventList = orderDto.getItems();

        if (orderItemEventList.isEmpty()) {
            throw new InvalidOrderItemsException(INVALID_ORDER_ITEMS_MESSAGE,
                    INVALID_ORDER_ITEMS);
        }

        for (OrderItemDto item : orderItemEventList) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND_MESSAGE,
                            DATA_NOT_FOUND));
            productService.updateStock(item.getProductId(),
                    (product.getStock() - item.getQuantity()));
        }
    }

    public BaseResultEvent createResultEvent(String eventId,Long orderId, String status, String message) {
        return BaseResultEvent.builder()
                .eventId(eventId)
                .status(status)
                .orderId(orderId)
                .reason(message)
                .build();
    }

}
