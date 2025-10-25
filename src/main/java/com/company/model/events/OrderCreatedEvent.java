package com.company.model.events;

import com.company.model.dto.OrderItemDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderCreatedEvent {
    private String eventId;
    private Long orderId;
    private Long userId;
    private BigDecimal totalPrice;
    private List<OrderItemDto> orderItemDtos;
}
