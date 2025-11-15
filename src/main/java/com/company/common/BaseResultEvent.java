package com.company.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseResultEvent {
    private String eventId;
    private Long orderId;
    private String status;
    private String reason;
}
