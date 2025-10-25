package com.company.model.events;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StockFailedEvent {
    private String eventId = UUID.randomUUID().toString();
    private Long orderId;
    private String reason;
}
