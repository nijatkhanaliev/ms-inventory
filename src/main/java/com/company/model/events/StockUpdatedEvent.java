package com.company.model.events;

import com.company.model.dto.OrderItemDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class StockUpdatedEvent {
    private String eventId = UUID.randomUUID().toString();
    private Long orderId;
    private Long userId;
    private BigDecimal totalPrice;
    private List<OrderItemDto> orderItemDtos;
}
