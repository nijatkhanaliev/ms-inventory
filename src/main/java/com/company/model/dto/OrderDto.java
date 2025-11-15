package com.company.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private BigDecimal totalAmount;
    private List<OrderItemDto> items;
}
