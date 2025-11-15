package com.company.common;

import com.company.model.dto.ResponseQueueInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseEvent <T> {
    private String eventId;
    private T payload;
    private ResponseQueueInfo responseQueueInfo;
}
