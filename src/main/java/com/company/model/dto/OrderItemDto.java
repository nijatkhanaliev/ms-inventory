package com.company.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderItemDto implements Serializable {
    private Long productId;
    private int quantity;

    public OrderItemDto(Long productId, int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

}
