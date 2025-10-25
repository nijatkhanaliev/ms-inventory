package com.company.model.events;

import com.company.model.dto.OrderItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PaymentFailedEvent {
    private Long orderId;
    private Long userId;
    private String reason;
    private List<OrderItemDto> orderItemDtos;
}